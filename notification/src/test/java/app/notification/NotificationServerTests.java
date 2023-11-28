package app.notification;

import app.notification.controller.NotificationController;
import app.notification.controller.NotificationControllerService;
import app.notification.model.NotificationService;
import app.notification.model.db.NotificationRepository;
import app.notification.model.db.NotificationRepositoryService;
import app.utils.feign_clients.security.SecurityFeignClient;
import app.utils.feign_clients.security.SecurityFeignClientService;
import app.utils.logger.AroundLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationServerTests {

    @Autowired
    private AroundLogger aroundLogger;

    @Autowired
    private SecurityFeignClient securityFeignClient;

    @Autowired
    private SecurityFeignClientService securityFeignClientService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRepositoryService notificationRepositoryService;

    @Autowired
    private NotificationControllerService notificationControllerService;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private NotificationService notificationService;

    @Test
    void contextLoads() {
        assertNotNull(aroundLogger);
        assertNotNull(securityFeignClient);
        assertNotNull(securityFeignClientService);
        assertNotNull(notificationRepository);
        assertNotNull(notificationRepositoryService);
        assertNotNull(notificationService);
        assertNotNull(notificationControllerService);
        assertNotNull(notificationController);
    }
}
