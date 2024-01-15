package app.telegram;

import app.telegram.bot.BotService;
import app.telegram.bot.api.MyTelegramLongPollingBot;
import app.telegram.bot.api.TelegramBotAuthorizationConfiguration;
import app.telegram.bot.api.TelegramBotInitializer;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.feign_client_adapters.ContactFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.NotificationFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.SendFeignClientServiceAdapter;
import app.telegram.bot.task.model.db.TaskRepository;
import app.telegram.bot.task.model.db.TaskRepositoryService;
import app.telegram.users.controller.TelegramUserController;
import app.telegram.users.controller.TelegramUserControllerService;
import app.telegram.users.controller.TelegramUserService;
import app.telegram.users.model.TelegramUserJwtUtil;
import app.telegram.users.model.db.TelegramUserRepository;
import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.utils.logger.AroundLogger;
import app.utils.services.contact.feign.ContactFeignClient;
import app.utils.services.contact.feign.ContactFeignClientService;
import app.utils.services.notification.feign.NotificationFeignClient;
import app.utils.services.notification.feign.NotificationFeignClientService;
import app.utils.services.security.telegram.feign.SecurityTelegramUserFeignClient;
import app.utils.services.security.telegram.feign.SecurityTelegramUserFeignClientService;
import app.utils.services.sender.feign.SendFeignClient;
import app.utils.services.sender.feign.SendFeignClientService;
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

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRepositoryService taskRepositoryService;

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
        assertNotNull(taskRepository);
        assertNotNull(taskRepositoryService);
    }
}
