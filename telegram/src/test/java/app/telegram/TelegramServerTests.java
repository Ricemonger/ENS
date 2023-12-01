package app.telegram;

import app.telegram.bot.BotService;
import app.telegram.bot.api.MyTelegramLongPollingBot;
import app.telegram.bot.api.TelegramBotAuthorizationConfiguration;
import app.telegram.bot.api.TelegramBotInitializer;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.feign_client_wrappers.ContactFeignClientServiceAdapter;
import app.telegram.bot.feign_client_wrappers.NotificationFeignClientServiceAdapter;
import app.telegram.bot.feign_client_wrappers.SendFeignClientServiceAdapter;
import app.telegram.users.controller.TelegramUserController;
import app.telegram.users.controller.TelegramUserControllerService;
import app.telegram.users.model.TelegramUserJwtUtil;
import app.telegram.users.model.TelegramUserService;
import app.telegram.users.model.db.TelegramUserRepository;
import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.telegram.users.model.security_telegram_client.SecurityTelegramUserFeignClient;
import app.telegram.users.model.security_telegram_client.SecurityTelegramUserFeignClientService;
import app.utils.feign_clients.contact.ContactFeignClient;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.NotificationFeignClient;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import app.utils.feign_clients.sender.SendFeignClient;
import app.utils.feign_clients.sender.SendFeignClientService;
import app.utils.logger.AroundLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TelegramServerTests {

    @Autowired
    private AroundLogger aroundLogger;

    @Autowired
    private MyTelegramLongPollingBot myTelegramLongPollingBot;

    @Autowired
    private TelegramBotAuthorizationConfiguration telegramBotAuthorizationConfiguration;

    @Autowired
    private TelegramBotInitializer telegramBotInitializer;

    @Autowired
    private BotCommandsConfig botCommandsConfig;

    @Autowired
    private ContactFeignClient contactFeignClient;

    @Autowired
    private ContactFeignClientService contactFeignClientService;

    @Autowired
    private ContactFeignClientServiceAdapter contactFeignClientServiceWrapper;

    @Autowired
    private NotificationFeignClient notificationFeignClient;

    @Autowired
    private NotificationFeignClientService notificationFeignClientService;

    @Autowired
    private NotificationFeignClientServiceAdapter notificationFeignClientServiceAdapter;

    @Autowired
    private SendFeignClient sendFeignClient;

    @Autowired
    private SendFeignClientService sendFeignClientService;

    @Autowired
    private SendFeignClientServiceAdapter sendFeignClientServiceAdapter;

    @Autowired
    private BotService botService;

    @Autowired
    private TelegramUserController telegramUserController;

    @Autowired
    private TelegramUserControllerService telegramUserControllerService;

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    private TelegramUserRepositoryService telegramUserRepositoryService;

    @Autowired
    private SecurityTelegramUserFeignClient securityTelegramUserFeignClient;

    @Autowired
    private SecurityTelegramUserFeignClientService securityTelegramUserFeignClientService;

    @Autowired
    private TelegramUserJwtUtil telegramUserJwtUtil;

    @Autowired
    private TelegramUserService telegramUserService;

    @Test
    void contextLoads() {
        assertNotNull(aroundLogger);
        assertNotNull(myTelegramLongPollingBot);
        assertNotNull(telegramBotAuthorizationConfiguration);
        assertNotNull(telegramBotInitializer);
        assertNotNull(botCommandsConfig);
        assertNotNull(contactFeignClient);
        assertNotNull(contactFeignClientService);
        assertNotNull(contactFeignClientServiceWrapper);
        assertNotNull(notificationFeignClient);
        assertNotNull(notificationFeignClientService);
        assertNotNull(notificationFeignClientServiceAdapter);
        assertNotNull(sendFeignClient);
        assertNotNull(sendFeignClientService);
        assertNotNull(sendFeignClientServiceAdapter);
        assertNotNull(botService);
        assertNotNull(telegramUserController);
        assertNotNull(telegramUserControllerService);
        assertNotNull(telegramUserRepository);
        assertNotNull(telegramUserRepositoryService);
        assertNotNull(securityTelegramUserFeignClient);
        assertNotNull(securityTelegramUserFeignClientService);
        assertNotNull(telegramUserJwtUtil);
        assertNotNull(telegramUserService);
    }
}
