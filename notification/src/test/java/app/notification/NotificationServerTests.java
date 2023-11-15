package app.notification;

import app.notification.controller.NotificationController;
import app.notification.controller.NotificationControllerService;
import app.notification.model.NotificationService;
import app.notification.model.db.NotificationRepository;
import app.notification.model.db.NotificationRepositoryService;
import app.utils.feign_clients.security.SecurityJwtWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationServerTests {

    @MockBean
    private SecurityJwtWebClient securityJwtWebClient;

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
        assertNotNull(securityJwtWebClient);
        assertNotNull(notificationRepository);
        assertNotNull(notificationRepositoryService);
        assertNotNull(notificationControllerService);
        assertNotNull(notificationController);
        assertNotNull(notificationService);
    }
}
