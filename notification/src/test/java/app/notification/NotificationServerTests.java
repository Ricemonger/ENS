package app.notification;

import app.notification.controller.NotificationController;
import app.notification.controller.NotificationControllerService;
import app.notification.model.db.NotificationRepository;
import app.notification.model.db.NotificationRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationServerTests {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRepositoryService notificationRepositoryService;

    @Autowired
    private NotificationControllerService notificationControllerService;

    @Autowired
    private NotificationController notificationController;

    @Test
    void contextLoads() {
        assertNotNull(notificationRepository);
        assertNotNull(notificationRepositoryService);
        assertNotNull(notificationControllerService);
        assertNotNull(notificationController);
    }
}
