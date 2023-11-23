package app.security;

import app.security.abstract_users.controller.AbstractUserController;
import app.security.abstract_users.controller.AbstractUserControllerService;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.model.AnyUserService;
import app.security.any_users.model.db.AnyUserRepository;
import app.security.any_users.model.db.AnyUserRepositoryService;
import app.security.ens_users.controller.EnsUserController;
import app.security.ens_users.controller.EnsUserControllerService;
import app.security.ens_users.model.EnsUserService;
import app.security.ens_users.model.db.EnsUserRepository;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.security.ens_users.model.security.EnsUserDetailsService;
import app.security.ens_users.model.security.EnsUserPasswordEncoderConfiguration;
import app.security.ens_users.model.security.EnsUserSecurityConfiguration;
import app.security.tg_users.controller.TelegramUserController;
import app.security.tg_users.controller.TelegramUserControllerService;
import app.security.tg_users.model.TelegramUserService;
import app.security.tg_users.model.db.TelegramUserRepository;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_module_client.TelegramFeignClient;
import app.security.tg_users.model.telegram_module_client.TelegramFeignClientService;
import app.utils.feign_clients.contact.ContactFeignClient;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.NotificationFeignClient;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SecurityServerTests {

    @Autowired
    private AbstractUserJwtUtil abstractUserJwtUtil;

    @Autowired
    private AbstractUserController abstractUserController;

    @Autowired
    private AbstractUserControllerService abstractUserControllerService;

    @Autowired
    private AnyUserRepositoryService anyUserRepositoryService;

    @Autowired
    private AnyUserRepository anyUserRepository;

    @Autowired
    private AnyUserService anyUserService;

    @Autowired
    private EnsUserController ensUserController;

    @Autowired
    private EnsUserControllerService ensUserControllerService;

    @Autowired
    private EnsUserRepositoryService ensUserRepositoryService;

    @Autowired
    private EnsUserRepository ensUserRepository;

    @Autowired
    private EnsUserDetailsService ensUserDetailsService;

    @Autowired
    private EnsUserPasswordEncoderConfiguration ensUserPasswordEncoderConfiguration;

    @Autowired
    private EnsUserSecurityConfiguration ensUserSecurityConfiguration;

    @Autowired
    private EnsUserService ensUserService;

    @Autowired
    private TelegramUserController telegramUserController;

    @Autowired
    private TelegramUserControllerService telegramUserControllerService;

    @Autowired
    private TelegramUserRepositoryService telegramUserRepositoryService;

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    private TelegramFeignClientService telegramFeignClientService;

    @Autowired
    private TelegramFeignClient telegramFeignClient;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private NotificationFeignClient notificationFeignClient;

    @Autowired
    private NotificationFeignClientService notificationFeignClientService;

    @Autowired
    private ContactFeignClient contactFeignClient;

    @Autowired
    private ContactFeignClientService contactFeignClientService;

    @Test
    public void contextLoads() {
        assertNotNull(abstractUserJwtUtil);
        assertNotNull(abstractUserController);
        assertNotNull(abstractUserControllerService);
        assertNotNull(anyUserRepositoryService);
        assertNotNull(anyUserRepository);
        assertNotNull(ensUserController);
        assertNotNull(ensUserControllerService);
        assertNotNull(ensUserRepositoryService);
        assertNotNull(ensUserRepository);
        assertNotNull(ensUserDetailsService);
        assertNotNull(ensUserPasswordEncoderConfiguration);
        assertNotNull(ensUserSecurityConfiguration);
        assertNotNull(telegramUserController);
        assertNotNull(telegramUserControllerService);
        assertNotNull(telegramUserRepositoryService);
        assertNotNull(telegramUserRepository);
        assertNotNull(telegramFeignClientService);
        assertNotNull(telegramFeignClient);
        assertNotNull(notificationFeignClient);
        assertNotNull(notificationFeignClientService);
        assertNotNull(contactFeignClient);
        assertNotNull(contactFeignClientService);
        assertNotNull(anyUserService);
        assertNotNull(ensUserService);
        assertNotNull(telegramUserService);
    }
}
