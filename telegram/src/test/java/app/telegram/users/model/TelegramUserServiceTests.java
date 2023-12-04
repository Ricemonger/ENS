package app.telegram.users.model;

import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.utils.services.security.telegram.feign.SecurityTelegramUserFeignClient;
import app.utils.services.security.telegram.feign.SecurityTelegramUserFeignClientService;
import app.utils.services.telegram.exceptions.InvalidTelegramTokenException;
import app.utils.services.telegram.exceptions.TelegramUserAlreadyExistsException;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TelegramUserServiceTests {

    private final static Long CHAT_ID = 9999L;

    private final static String MOCK_STRING = "MOCK_STRING";

    private final InputGroup INPUT_GROUP = InputGroup.LINK;

    private final InputState INPUT_STATE = InputState.NOTIFICATION_NAME;

    @SpyBean
    private TelegramUserRepositoryService repositoryService;

    @SpyBean
    private TelegramUserJwtUtil jwtUtil;

    private MockSecurityTelegramUserFeignClientService f;

    private MockSecurityTelegramUserFeignClientService securityService;

    private TelegramUserService userService;

    @BeforeEach
    public void setUp() {
        repositoryService.deleteAll();

        f = new MockSecurityTelegramUserFeignClientService(null);

        securityService = spy(f);

        userService = new TelegramUserService(repositoryService, jwtUtil,
                securityService);
    }

    @Test
    public void createShouldSaveInRepositoryAndInSecurityModule() {
        TelegramUser returned = userService.create(CHAT_ID);

        System.out.println(returned);

        verify(repositoryService).save(new TelegramUser(String.valueOf(CHAT_ID)));

        verify(jwtUtil).generateToken(CHAT_ID);

        verify(securityService).create(anyString());

        TelegramUser inDb = repositoryService.findAll().get(0);

        assertEquals(inDb, returned);
    }

    @Test
    public void createShouldThrowIfUserAlreadyExists() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        Executable executable = () -> userService.create(CHAT_ID);

        assertThrows(TelegramUserAlreadyExistsException.class, executable);
    }

    @Test
    public void getChatIdByTokenShouldReturnIfValidToken() {
        String token = jwtUtil.generateToken(CHAT_ID);

        String chatId = userService.getChatIdByToken(token);

        assertEquals(String.valueOf(CHAT_ID), chatId);

        verify(jwtUtil).extractChatId(token);
    }

    @Test
    public void getChatIdByTokenShouldThrowIfInvalidToken() {
        Executable executableInvalid = () -> userService.getChatIdByToken("token.token.token");

        assertThrows(InvalidTelegramTokenException.class, executableInvalid);

        Executable executableMalformed = () -> userService.getChatIdByToken("token.token");

        assertThrows(InvalidTelegramTokenException.class, executableMalformed);

        Executable executableNull = () -> userService.getChatIdByToken(null);

        assertThrows(InvalidTelegramTokenException.class, executableNull);
    }

    @Test
    public void getAccountInfoShouldGetInfoFromSecurityModule() {
        String token = jwtUtil.generateToken(CHAT_ID);

        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID), token, null, null, null));

        String accountInfo = userService.getAccountInfo(CHAT_ID);

        verify(securityService).getAccountInfo(token);

        assertEquals(MOCK_STRING, accountInfo);
    }

    @Test
    public void getAccountInfoShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.getAccountInfo(CHAT_ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void removeAccountShouldDeleteFromRepositoryAndSecurityModule() {
        userService.removeAccount(CHAT_ID);

        verify(securityService).delete(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)));

        verify(repositoryService).delete(new TelegramUser(String.valueOf(CHAT_ID)));
    }

    @Test
    public void linkShouldCallSecurityLink() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        userService.link(CHAT_ID, "username", "password");

        verify(securityService).link(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)), eq("username"),
                eq("password"));
    }

    @Test
    public void linkShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.link(CHAT_ID, "username", "password");

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void unlinkShouldCallSecurityUnlink() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        userService.unlink(CHAT_ID);

        verify(securityService).unlink(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)));
    }

    @Test
    public void unlinkShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.unlink(CHAT_ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void isLinkedShouldReturnSecurityIsLinkedResult() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));
        securityService.isLinkedBool = false;
        boolean result1 = userService.isLinked(CHAT_ID);

        assertFalse(result1);

        verify(securityService).isLinked(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)));

        securityService.isLinkedBool = true;
        boolean result2 = userService.isLinked(CHAT_ID);

        assertTrue(result2);
    }

    @Test
    public void isLinkedShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.isLinked(CHAT_ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void setBaseInputAndGroupForAllUsersShouldSet() {
        List<TelegramUser> added = new ArrayList<>();
        added.add(new TelegramUser("224242", InputState.USERNAME, InputGroup.CONTACT_ADD_ONE));
        added.add(new TelegramUser("65656", InputState.BASE, InputGroup.LINK));
        added.add(new TelegramUser("232", InputState.PASSWORD, InputGroup.BASE));

        added.forEach(user -> repositoryService.save(user));

        List<TelegramUser> expected = new ArrayList<>();

        added.forEach(user -> expected.add(new TelegramUser(user.getChatId(), InputState.BASE, InputGroup.BASE)));

        userService.setBaseInputAndGroupForAllUsers();

        List<TelegramUser> inDb = repositoryService.findAll();

        assertTrue(inDb.containsAll(expected) && expected.containsAll(inDb));

    }

    @Test
    public void setNextInputGroupShouldSetInDb() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        userService.setInputGroup(CHAT_ID, INPUT_GROUP);

        TelegramUser user = repositoryService.findByChatIdOrThrow(String.valueOf(CHAT_ID));
        assertEquals(INPUT_GROUP, user.getInputGroup());
    }

    @Test
    public void setNextInputGroupShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.setInputGroup(CHAT_ID, INPUT_GROUP);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void setNextInputShouldSetInDb() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        userService.setInputState(CHAT_ID, INPUT_STATE);

        TelegramUser user = repositoryService.findByChatIdOrThrow(String.valueOf(CHAT_ID));
        assertEquals(INPUT_STATE, user.getInputState());
    }

    @Test
    public void setNextInputShouldThrowIfDoesntExist() {
        Executable executable = () -> userService.setInputState(CHAT_ID, INPUT_STATE);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    @Test
    public void getInputGroupShouldReturnRightGroup() {
        TelegramUser user = new TelegramUser(String.valueOf(CHAT_ID));
        user.setInputGroup(INPUT_GROUP);
        repositoryService.save(user);

        assertEquals(INPUT_GROUP, userService.getInputGroupOrBase(CHAT_ID));
    }

    @Test
    public void getInputGroupShouldReturnBaseIfDoesntExist() {
        InputGroup group = userService.getInputGroupOrBase(CHAT_ID);

        assertEquals(InputGroup.BASE, group);
    }

    @Test
    public void getInputStateShouldReturnRightGroup() {
        TelegramUser user = new TelegramUser(String.valueOf(CHAT_ID));
        user.setInputState(INPUT_STATE);
        repositoryService.save(user);

        assertEquals(INPUT_STATE, userService.getInputStateOrBase(CHAT_ID));
    }

    @Test
    public void getInputStateShouldReturnBaseIfDoesntExist() {
        InputState state = userService.getInputStateOrBase(CHAT_ID);

        assertEquals(InputState.BASE, state);
    }

    @Test
    public void doesUserExistsShouldReturnTrueIfExists() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        assertTrue(userService.doesUserExist(CHAT_ID));
    }

    @Test
    public void doesUserExistsShouldReturnFalseIfDoesntExist() {
        assertFalse(userService.doesUserExist(CHAT_ID));
    }

    @Test
    public void findSecurityTokenOrGenerateAndPutShouldGetSecurityTokenFromSecurityModuleAndPutInDb() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        String securityToken = userService.findSecurityTokenOrGenerateAndPut(CHAT_ID);

        verify(securityService).getSecurityToken(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)));

        assertEquals(MOCK_STRING, securityToken);
    }

    @Test
    public void findSecurityTokenOrGenerateAndPutShouldThrowInDoesntExist() {
        Executable executable = () -> userService.findSecurityTokenOrGenerateAndPut(CHAT_ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
    }

    private static class MockSecurityTelegramUserFeignClientService extends SecurityTelegramUserFeignClientService {

        public boolean isLinkedBool = false;

        public MockSecurityTelegramUserFeignClientService(SecurityTelegramUserFeignClient securityTelegramUserFeignClient) {
            super(null);
        }

        public String create(String telegramToken) {
            return MOCK_STRING;
        }

        public void delete(String telegramToken) {
        }

        public void link(String telegramToken, String username, String password) {
            isLinkedBool = true;
        }

        public void unlink(String telegramToken) {
            isLinkedBool = false;
        }

        public boolean isLinked(String telegramToken) {
            return isLinkedBool;
        }

        public String getSecurityToken(String telegramToken) {
            return MOCK_STRING;
        }

        public String getAccountInfo(String telegramToken) {
            return MOCK_STRING;
        }

        @Override
        public boolean doesUserExists(String telegramToken) {
            return true;
        }
    }
}
