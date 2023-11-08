package app.notification.service;

import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.service.db.NotificationRepository;
import app.notification.service.db.NotificationRepositoryService;
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
class NotificationFeignServiceTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private NotificationRepositoryService notificationRepositoryService;

    @BeforeEach
    void setUp() {
        notificationRepositoryService = new NotificationRepositoryService(notificationRepository);
    }

    @Test
    void createThrowsExceptionIfExists() {
        Notification notification = new Notification("username", "name", "text");
        notificationRepository.save(notification);
        Executable executable = () -> notificationRepositoryService.create(notification);
        assertThrows(NotificationAlreadyExistsException.class, executable);
    }

    @Test
    void createNormalBehavior() {
        Notification notification = new Notification("username", "name", "text");
        notificationRepositoryService.create(notification);
        assertTrue(notificationRepository.findAll().contains(notification));
    }

    @Test
    void updateThrowsExceptionIfNotExists() {
        Notification notification = new Notification("username", "name", "text");
        Executable executable = () -> notificationRepositoryService.update(notification);
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void updateNormalBehavior() {
        String accountId = "accountId";
        String name = "name";
        String originalText = "text";
        String alteredText = "TEXT1";
        Notification notification = new Notification(accountId, name, originalText);
        Notification altered = new Notification(accountId, name, alteredText);
        notificationRepository.save(notification);
        Executable executable = () -> notificationRepositoryService.update(altered);
        assertDoesNotThrow(executable);
        assertEquals(altered, notificationRepository.findAll().get(0));
    }


    @Test
    void deleteThrowsExceptionIfNotExists() {
        Notification notification = new Notification("username", "name", "text");
        Executable executable = () -> notificationRepositoryService.delete(notification);
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void deleteNormalBehavior() {
        Notification notification = new Notification("username", "name", "text");
        notificationRepository.save(notification);
        Executable executable = () -> notificationRepositoryService.delete(notification);
        assertDoesNotThrow(executable);
        assertEquals(notificationRepository.findAll(), Collections.emptyList());
    }

    @Test
    void findOneByPrimaryKeyNormalBehavior() {
        Notification not = new Notification("username", "name", "text");
        notificationRepository.save(not);
        notificationRepository.save(new Notification("username", "name1", "text"));
        notificationRepository.save(new Notification("username1", "name", "text"));
        Notification notification = notificationRepositoryService.findOneByPrimaryKey("username", "name");
        assertEquals(notification, not);
    }

    @Test
    void findOneByPrimaryKeyThrowsExceptionIfNotExists() {
        notificationRepository.save(new Notification("username", "name1", "text"));
        notificationRepository.save(new Notification("username1", "name", "text"));
        Executable executable = () -> {
            notificationRepositoryService.findOneByPrimaryKey("username", "name");
        };
        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    void findAllByAccountId() {
        Notification not1 = new Notification("username", "name", "text");
        Notification not2 = new Notification("username", "name1", "text");
        notificationRepository.save(not1);
        notificationRepository.save(not2);
        notificationRepository.save(new Notification("username1", "name", "text"));
        List<Notification> result = new ArrayList<>();
        result.add(not1);
        result.add(not2);
        assertEquals(result, notificationRepositoryService.findAllByAccountId("username"));
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
        assertEquals(result, notificationRepositoryService.findAllLikePrimaryKey("username", "name"));
    }
}