package app.security;

import app.security.abstract_users.controller.AbstractUserController;
import app.security.abstract_users.controller.AbstractUserControllerService;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.db.AnyUserRepository;
import app.security.any_users.db.AnyUserRepositoryService;
import app.security.ens_users.controller.EnsUserController;
import app.security.ens_users.controller.EnsUserControllerService;
import app.security.ens_users.db.EnsUserRepository;
import app.security.ens_users.db.EnsUserRepositoryService;
import app.security.ens_users.security.EnsUserDetailsService;
import app.security.ens_users.security.EnsUserJwtAuthFilter;
import app.security.ens_users.security.EnsUserPasswordEncoderConfiguration;
import app.security.ens_users.security.EnsUserSecurityConfiguration;
import app.security.tg_users.controller.TelegramUserController;
import app.security.tg_users.controller.TelegramUserControllerService;
import app.security.tg_users.model.db.TelegramUserRepository;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_service_client.TelegramFeignClient;
import app.security.tg_users.model.telegram_service_client.TelegramFeignClientService;
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
    private EnsUserJwtAuthFilter ensUserJwtAuthFilter;

    @Autowired
    private EnsUserPasswordEncoderConfiguration ensUserPasswordEncoderConfiguration;

    @Autowired
    private EnsUserSecurityConfiguration ensUserSecurityConfiguration;

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
        assertNotNull(ensUserJwtAuthFilter);
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
    }
}
