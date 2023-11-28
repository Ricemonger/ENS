package app.notification.model.db;

import app.notification.model.Notification;
import app.utils.feign_clients.notification.exceptions.NotificationDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class NotificationRepositoryServiceTests {

    private final static String ACCOUNT_ID = "1111";

    private final static String ANOTHER_ACCOUNT_ID = "9999";

    private final static String NAME = "name";

    private final static String TEXT = "text";

    private final static Notification NOTIFICATION = new Notification(ACCOUNT_ID, NAME, TEXT);

    private final static NotificationEntity ENTITY = new NotificationEntity(ACCOUNT_ID, NAME, TEXT);

    private final static NotificationEntity ANOTHER_ENTITY = new NotificationEntity(ANOTHER_ACCOUNT_ID, NAME, TEXT);

    @SpyBean
    private NotificationRepository repository;

    @Autowired
    private NotificationRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void save() {
        service.save(NOTIFICATION);

        verify(repository).save(ENTITY);

        assertEquals(new Notification(repository.findAll().get(0)), NOTIFICATION);
    }

    @Test
    public void doesNotificationExistsShouldReturnTrueIfExists() {
        repository.save(ENTITY);

        assertTrue(service.doesNotificationExist(NOTIFICATION));
    }

    @Test
    public void doesNotificationExistsShouldReturnFalseIfDoesntExist() {
        repository.save(ANOTHER_ENTITY);

        assertFalse(service.doesNotificationExist(NOTIFICATION));
    }

    @Test
    public void findByIdOrThrow() {
        repository.save(ENTITY);

        Notification notification = service.findByIdOrThrow(ACCOUNT_ID, NAME);

        assertEquals(new Notification(repository.findAll().get(0)), notification);
    }

    @Test
    public void findByIdOrThrowShouldThrowIfDoesntExist() {
        Executable executable = () -> service.findByIdOrThrow(ACCOUNT_ID, NAME);

        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    public void findAllByAccountId() {
        Notification expected = new Notification(repository.save(ENTITY));
        repository.save(ANOTHER_ENTITY);

        List<Notification> expectedResult = Collections.singletonList(expected);

        List<Notification> trueResult = service.findAllByAccountId(ACCOUNT_ID);

        assertEquals(expectedResult, trueResult);

        verify(repository).findAllByAccountId(ACCOUNT_ID);
    }

    @Test
    public void findAll() {
        repository.save(ENTITY);
        repository.save(ANOTHER_ENTITY);

        int size = service.findAll().size();

        assertEquals(2, size);

        verify(repository).findAll();
    }

    @Test
    public void delete() {
        repository.save(ENTITY);
        repository.save(ANOTHER_ENTITY);

        service.delete(NOTIFICATION);

        assertEquals(repository.findAll(), Collections.singletonList(ANOTHER_ENTITY));

        verify(repository).delete(ENTITY);
    }

    @Test
    public void deleteAll() {
        reset(repository);

        service.deleteAll();

        verify(repository).deleteAll();
    }

    @Test
    public void deleteAllList() {
        reset(repository);

        repository.save(ENTITY);

        service.deleteAll(Collections.singletonList(NOTIFICATION));

        verify(repository).deleteAll(Collections.singletonList(ENTITY));
    }
}
