package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.service.Notification;
import app.notification.service.db.NotificationCompositeKey;
import app.notification.service.db.NotificationRepository;
import app.notification.service.db.NotificationService;
import app.utils.JwtClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private JwtClient jwtClient;

    @Autowired
    private NotificationRepository notificationRepository;

    private NotificationService notificationService;

    private NotificationController notificationController;

    @Mock
    private NotificationService mockNotificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
        notificationController = new NotificationController(notificationService, jwtClient);
    }

    @Test
    void create() {
        String token = "token";
        NotificationCreUpdRequest request = new NotificationCreUpdRequest("name", "text");
        notificationController.create(token, request);
        verify(jwtClient).extractAccountId(token);
        Notification notification1 = new Notification(null, request.name(), request.text());
        assertEquals(notification1, notificationRepository.findById(new NotificationCompositeKey(null, request.name())).orElseThrow());
        Executable executable = () -> notificationController.create(token, request);
        assertThrows(NotificationAlreadyExistsException.class, executable);
    }

    @Test
    void update() {
        String token = "token";
        NotificationCreUpdRequest request = new NotificationCreUpdRequest("name", "text");
        Executable executable = () -> notificationController.update(token, request);
        assertThrows(NotificationDoesntExistException.class, executable);
        notificationController.create(token, request);
        NotificationCreUpdRequest anotherRequest = new NotificationCreUpdRequest("name", "newNotificationText");
        Notification anotherNotification = new Notification(null, "name", "newNotificationText");
        reset(jwtClient);
        notificationController.update(token, anotherRequest);
        verify(jwtClient).extractAccountId(token);
        assertEquals(notificationRepository.findById(new NotificationCompositeKey(null, anotherRequest.name())).orElseThrow(), anotherNotification);
    }

    @Test
    void delete() {
        String token = "token";
        NotificationNameRequest request = new NotificationNameRequest("name");
        Executable executable = () -> notificationController.delete(token, request);
        assertThrows(NotificationDoesntExistException.class, executable);
        notificationRepository.save(new Notification(null, request.name()));
        notificationController.delete(token, request);
        Executable executableAfter = () -> notificationRepository.findById(new NotificationCompositeKey(null, request.name())).orElseThrow();
        assertThrows(NoSuchElementException.class, executableAfter);
    }

    @Test
    void findAllByAccountId() {
        notificationController = new NotificationController(mockNotificationService, jwtClient);
        notificationController.findAllByAccountId("");
        verify(mockNotificationService).findAllByAccountId(any());
    }

    @Test
    void findAllLikePrimaryKey() {
        notificationController = new NotificationController(mockNotificationService, jwtClient);
        String name = "name";
        notificationController.findAllLikePrimaryKey("", new NotificationNameRequest(name));
        verify(mockNotificationService).findAllLikePrimaryKey(any(), eq(name));
    }
}