package app.notification;

import app.notification.controller.NotificationController;
import app.notification.service.db.NotificationRepository;
import app.notification.service.db.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationServerTests {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationController notificationController;

    @Test
    void contextLoads() throws Exception {
        assertNotNull(notificationRepository);
        assertNotNull(notificationService);
        assertNotNull(notificationController);
    }

}
