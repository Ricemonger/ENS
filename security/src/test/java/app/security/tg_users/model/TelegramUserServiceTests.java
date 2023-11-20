package app.security.tg_users.model;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.model.db.AnyUserRepositoryService;
import app.security.ens_users.EnsUser;
import app.security.ens_users.model.EnsUserService;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_module_client.TelegramFeignClient;
import app.security.tg_users.model.telegram_module_client.TelegramFeignClientService;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TelegramUserServiceTests {

    private static final String TOKEN = "TELEGRAM TOKEN";

    private static final String USERNAME = "USERNAME";

    private static final String PASSWORD = "PASSWORD";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnsUserRepositoryService ensUserRepositoryService;

    @Autowired
    private AnyUserRepositoryService anyUserRepositoryService;

    @SpyBean
    private TelegramUserRepositoryService telegramUserRepositoryService;

    @SpyBean
    private EnsUserService ensUserService;

    @Spy
    private final TelegramFeignClientService telegramFeignClientService = new MockTelegramFeignClientService(null);

    @MockBean
    private ContactFeignClientService contactFeignClientService;

    @MockBean
    private NotificationFeignClientService notificationFeignClientService;

    @SpyBean
    private AbstractUserJwtUtil abstractUserJwtUtil;

    private TelegramUserService telegramUserService;

    @BeforeEach
    public void setUp() {
        telegramUserService = new TelegramUserService(
                telegramUserRepositoryService,
                ensUserService,
                telegramFeignClientService,
                contactFeignClientService,
                notificationFeignClientService,
                abstractUserJwtUtil);
        telegramUserRepositoryService.deleteAll();
        ensUserRepositoryService.deleteAll();
        anyUserRepositoryService.deleteAll();
    }

    @Test
    public void createShouldAddNewUserIfUserDoesntExist() {
        String token = telegramUserService.create(TOKEN);
        String accountId = telegramUserRepositoryService.findAll().get(0).getAccountId();

        verify(telegramFeignClientService).getChatId(TOKEN);

        verify(telegramUserRepositoryService).save(new TelegramUser(TOKEN));

        assertTrue(abstractUserJwtUtil.isTokenValidAndContainsAccountId(token, accountId));
    }

    @Test
    public void createShouldThrowIfUserAlreadyExists() {
        telegramUserRepositoryService.save(new TelegramUser(TOKEN));

        Executable executable = () -> telegramUserService.create(TOKEN);

        assertThrows(UserAlreadyExistsException.class, executable);
    }

    @Test
    public void deleteShouldRemoveUserFromDb() {
        telegramUserRepositoryService.save(new TelegramUser(TOKEN));

        telegramUserService.delete(TOKEN);

        assertEquals(0, telegramUserRepositoryService.findAll().size());
    }

    @Test
    public void deleteShouldThrowIfUserDoesntExist() {
        Executable executable = () -> telegramUserService.delete(TOKEN);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void generateSecurityTokenShouldReturnValidToken() {
        telegramUserRepositoryService.save(new TelegramUser(TOKEN));
        String accountId = telegramUserRepositoryService.findAll().get(0).getAccountId();

        String securityToken = telegramUserService.getSecurityToken(TOKEN);

        assertTrue(abstractUserJwtUtil.isTokenValidAndContainsAccountId(securityToken, accountId));
    }

    @Test
    public void generateSecurityTokenShouldThrowIfUserDoesntExist() {
        Executable executable = () -> telegramUserService.getSecurityToken(TOKEN);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void getAccountInfoShouldFindInfo() {
        String accountId = telegramUserRepositoryService.save(new TelegramUser(TOKEN)).getAccountId();

        telegramUserService.getAccountInfo(TOKEN);

        verify(abstractUserJwtUtil).generateToken(accountId);

        verify(contactFeignClientService).findAllById(anyString());

        verify(notificationFeignClientService).findAllById(anyString());
    }

    @Test
    public void getAccountInfoShouldThrowIfDoesntExist() {
        Executable executable = () -> telegramUserService.getAccountInfo(TOKEN);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void linkShouldChangeAccountIdsIfAuthorized() {
        String username = USERNAME;
        String password = PASSWORD;
        ensUserService.register(new EnsUser(username, password));

        String oldAccountId = telegramUserRepositoryService.save(new TelegramUser(TOKEN)).getAccountId();
        String newAccountId = ensUserService.getByUsernameOrThrow(username).getAccountId();

        reset(abstractUserJwtUtil);
        telegramUserService.link(TOKEN, username, password);

        assertEquals(newAccountId, telegramUserRepositoryService.findAll().get(0).getAccountId());

        verify(ensUserService).canLogin(username, password);
        verify(abstractUserJwtUtil).generateToken(oldAccountId);
        verify(abstractUserJwtUtil).generateToken(newAccountId);
        verify(contactFeignClientService).changeAccountId(anyString(), anyString());
        verify(notificationFeignClientService).changeAccountId(anyString(), anyString());
    }

    @Test
    public void linkShouldThrowUserDoesntExistOrBadCredentials() {
        telegramUserRepositoryService.save(new TelegramUser(TOKEN));

        Executable executable = () -> telegramUserService.link(TOKEN, USERNAME, PASSWORD);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void isLinkedShouldReturnTrueIfAccountHasLinking() {
        String username = USERNAME;
        String password = PASSWORD;
        ensUserService.register(new EnsUser(username, password));

        String accountId = ensUserService.getByUsernameOrThrow(username).getAccountId();

        telegramUserRepositoryService.save(new TelegramUser(accountId, TOKEN));

        assertTrue(telegramUserService.isLinked(TOKEN));
    }

    @Test
    public void isLinkedShouldReturnFalseIfAccountIsNotLinked() {
        String username = USERNAME;
        String password = PASSWORD;
        ensUserService.register(new EnsUser(username, password));

        telegramUserRepositoryService.save(new TelegramUser(TOKEN));

        assertFalse(telegramUserService.isLinked(TOKEN));
    }

    @Test
    public void unlinkWithDataToTelegramShouldChangeAccountIdsToTelegramAndNotChangeEnsUserAccountId() {
        String username = USERNAME;
        String password = PASSWORD;

        String oldAccountId = telegramUserRepositoryService.save(new TelegramUser(TOKEN)).getAccountId();

        EnsUser ensUser = new EnsUser(oldAccountId, username, passwordEncoder.encode(password));
        ensUserRepositoryService.save(ensUser);

        telegramUserService.unlinkWithDataToTelegram(TOKEN);

        verify(abstractUserJwtUtil).generateToken(oldAccountId);
        verify(abstractUserJwtUtil).generateToken(telegramUserRepositoryService.findAll().get(0).getAccountId());
        verify(notificationFeignClientService).changeAccountId(anyString(), anyString());
        verify(contactFeignClientService).changeAccountId(anyString(), anyString());

        assertNotEquals(oldAccountId, telegramUserRepositoryService.findAll().get(0).getAccountId());
        assertEquals(oldAccountId, ensUserRepositoryService.findByIdOrThrow(username).getAccountId());
    }

    @Test
    public void unlinkWithDataToTelegramShouldNotThrowIfEnsUserDoesntExist() {
        telegramUserRepositoryService.save(new TelegramUser(TOKEN));

        Executable executable = () -> telegramUserService.unlinkWithDataToTelegram(TOKEN);

        assertDoesNotThrow(executable);
    }

    @Test
    public void unlinkWithDataToTelegramShouldThrowIfTelegramUserDoesntExist() {
        ensUserService.register(new EnsUser(USERNAME, PASSWORD));

        Executable executable = () -> telegramUserService.unlinkWithDataToTelegram(TOKEN);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void unlinkWithDataToEnsShouldChangeOnlyTelegramAccountId() {
        String oldAccountId = telegramUserRepositoryService.save(new TelegramUser(TOKEN)).getAccountId();

        telegramUserService.unlinkWithDataToEns(TOKEN);

        assertNotEquals(oldAccountId, telegramUserRepositoryService.findAll().get(0).getAccountId());

        verifyNoInteractions(contactFeignClientService, notificationFeignClientService, abstractUserJwtUtil);
    }

    @Test
    public void unlinkWithDataToEnsShouldThrowIfTelegramDoesntExist() {
        Executable executable = () -> telegramUserService.unlinkWithDataToEns(TOKEN);

        assertThrows(UserDoesntExistException.class, executable);
    }


    private static class MockTelegramFeignClientService extends TelegramFeignClientService {

        public MockTelegramFeignClientService(TelegramFeignClient telegramFeignClient) {
            super(telegramFeignClient);
        }

        @Override
        public String getChatId(String telegramToken) {
            return telegramToken;
        }
    }
}
