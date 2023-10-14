package app.notification.service;

import app.notification.controller.exceptions.NotificationAlreadyExistsException;
import app.notification.controller.exceptions.NotificationDoesntExistException;
import app.notification.model.Notification;
import app.notification.service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationServiceTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
    }

    @Test
    void createThrowsExceptionIfExists() {
        Notification notification = new Notification("username", "name", "text");
        notificationRepository.save(notification);
        Executable executable = () -> notificationService.create(notification);
        assertThrows(NotificationAlreadyExistsException.class, executable);
    }

    @Test
    void createNormalBehavior() {
        Notification notification = new Notification("username", "name", "text");
        notificationService.create(notification);
        assertTrue(notificationRepository.findAll().contains(notification));
    }

    @Test
    void updateThrowsExceptionIfNotExists() {
        Notification notification = new Notification("username", "name", "text");
        Executable executable = () -> notificationService.update(notification);
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void updateNormalBehavior() {
        String username = "username";
        String name = "name";
        String originalText = "text";
        String alteredText = "TEXT1";
        Notification notification = new Notification(username, name, originalText);
        Notification altered = new Notification(username, name, alteredText);
        notificationRepository.save(notification);
        Executable executable = () -> notificationService.update(altered);
        assertDoesNotThrow(executable);
        assertEquals(altered, notificationRepository.findAll().get(0));
    }


    @Test
    void deleteThrowsExceptionIfNotExists() {
        Notification notification = new Notification("username", "name", "text");
        Executable executable = () -> notificationService.delete(notification);
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void deleteNormalBehavior() {
        Notification notification = new Notification("username", "name", "text");
        notificationRepository.save(notification);
        Executable executable = () -> notificationService.delete(notification);
        assertDoesNotThrow(executable);
        assertEquals(notificationRepository.findAll(), Collections.emptyList());
    }

    @Test
    void findOneByPrimaryKeyNormalBehavior() {
        Notification not = new Notification("username", "name", "text");
        notificationRepository.save(not);
        notificationRepository.save(new Notification("username", "name1", "text"));
        notificationRepository.save(new Notification("username1", "name", "text"));
        Notification notification = notificationService.findOneByPrimaryKey("username", "name");
        assertEquals(notification, not);
    }

    @Test
    void findOneByPrimaryKeyThrowsExceptionIfNotExists() {
        notificationRepository.save(new Notification("username", "name1", "text"));
        notificationRepository.save(new Notification("username1", "name", "text"));
        Executable executable = () -> {
            notificationService.findOneByPrimaryKey("username", "name");
        };
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void findAllByUsername() {
        Notification not1 = new Notification("username", "name", "text");
        Notification not2 = new Notification("username", "name1", "text");
        notificationRepository.save(not1);
        notificationRepository.save(not2);
        notificationRepository.save(new Notification("username1", "name", "text"));
        List<Notification> result = new ArrayList<>();
        result.add(not1);
        result.add(not2);
        assertEquals(result, notificationService.findAllByUsername("username"));
    }

    @Test
    void findAllLikePrimaryKey() {
        Notification not1 = new Notification("username", "name", "text");
        Notification not2 = new Notification("username", "name1", "text");
        notificationRepository.save(not1);
        notificationRepository.save(not2);
        notificationRepository.save(new Notification("username1", "name", "text"));
        notificationRepository.save(new Notification("username1", "nam", "text"));
        notificationRepository.save(new Notification("usern", "name", "text"));
        List<Notification> result = new ArrayList<>();
        result.add(not1);
        result.add(not2);
        assertEquals(result, notificationService.findAllLikePrimaryKey("username", "name"));
    }
}