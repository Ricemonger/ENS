package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.controller.exceptions.NotificationAlreadyExistsException;
import app.notification.controller.exceptions.NotificationDoesntExistException;
import app.notification.model.Notification;
import app.notification.model.NotificationCompositeKey;
import app.notification.service.NotificationService;
import app.notification.service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utils.JwtClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void setUp(){
        notificationService = new NotificationService(notificationRepository);
        notificationController = new NotificationController(notificationService,jwtClient);
    }
    @Test
    void create() {
        String token = "token";
        NotificationCreUpdRequest request = new NotificationCreUpdRequest("name","text");
        Notification notification = notificationController.create(token,request);
        verify(jwtClient).extractUsername(token);
        Notification notification1 = new Notification(null,request.name(),request.text());
        assertEquals(notification1,notificationRepository.findById(new NotificationCompositeKey(null,request.name())).orElseThrow());
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                notificationController.create(token,request);
            }
        };
        assertThrows(NotificationAlreadyExistsException.class,executable);
    }

    @Test
    void update() {
        String token = "token";
        NotificationCreUpdRequest request = new NotificationCreUpdRequest("name","text");
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                notificationController.update(token,request);
            }
        };
        assertThrows(NotificationDoesntExistException.class,executable);
        notificationController.create(token,request);
        NotificationCreUpdRequest anotherRequest = new NotificationCreUpdRequest("name","newNotificationText");
        Notification anotherNotification = new Notification(null,"name","newNotificationText");
        reset(jwtClient);
        notificationController.update(token,anotherRequest);
        verify(jwtClient).extractUsername(token);
        assertEquals(notificationRepository.findById(new NotificationCompositeKey(null,anotherRequest.name())).orElseThrow(),anotherNotification);
    }

    @Test
    void delete() {
        String token = "token";
        NotificationNameRequest request = new NotificationNameRequest("name");
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                notificationController.delete(token,request);
            }
        };
        assertThrows(NotificationDoesntExistException.class,executable);
        notificationRepository.save(new Notification(null,request.name()));
        notificationController.delete(token,request);
        Executable executableAfter = new Executable() {
            @Override
            public void execute() throws Throwable {
                notificationRepository.findById(new NotificationCompositeKey(null,request.name())).orElseThrow();
            }
        };
        assertThrows(NoSuchElementException.class,executableAfter);
    }

    @Test
    void findAllByUsername() {
        notificationController = new NotificationController(mockNotificationService,jwtClient);
        notificationController.findAllByUsername("");
        verify(mockNotificationService).findAllByUsername(any());
    }

    @Test
    void findAllLikePrimaryKey() {
        notificationController = new NotificationController(mockNotificationService,jwtClient);
        String name = "name";
        notificationController.findAllLikePrimaryKey("",new NotificationNameRequest(name));
        verify(mockNotificationService).findAllLikePrimaryKey(any(),eq(name));
    }
}