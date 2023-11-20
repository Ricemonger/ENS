package app.telegram.users.model;

import app.telegram.users.exceptions.InvalidTelegramTokenException;
import app.telegram.users.exceptions.TelegramUserAlreadyExistsException;
import app.telegram.users.exceptions.TelegramUserDoesntExistException;
import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.telegram.users.model.security_client.SecurityTelegramUserFeignClient;
import app.telegram.users.model.security_client.SecurityTelegramUserFeignClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TelegramUserServiceTests {

    private final static Long CHAT_ID = 9999L;

    private final static String MOCK_STRING = "MOCK_STRING";

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
        Executable executable = () -> userService.getChatIdByToken("token.token.token");

        assertThrows(InvalidTelegramTokenException.class, executable);
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
    public void getSecurityTokenShouldGetSecurityTokenFromSecurityModuleAndPutInDb() {
        repositoryService.save(new TelegramUser(String.valueOf(CHAT_ID)));

        String securityToken = userService.getAndPutSecurityToken(CHAT_ID);

        verify(securityService).getSecurityToken(argThat(s -> jwtUtil.isTokenValidAndContainsChatId(s, CHAT_ID)));

        assertEquals(MOCK_STRING, securityToken);
    }

    @Test
    public void getSecurityTokenShouldThrowInDoesntExist() {
        Executable executable = () -> userService.getAndPutSecurityToken(CHAT_ID);

        assertThrows(TelegramUserDoesntExistException.class, executable);
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
    }
}
