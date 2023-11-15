package app.notification.model;

import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.model.db.NotificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTests {

    private final static Notification NOTIFICATION = new Notification("1111", "name", "text");

    private final static Notification ALTERED_NAME = new Notification("1111", "altered", "text");

    private final static Notification LIKE_NAME = new Notification("1111", "nameeeeee", "text");

    private final static Notification ALTERED_TEXT = new Notification("1111", "name", "altered");

    private final static Notification ALTERED_ACCOUNT_ID = new Notification("9999", "name", "text");

    @Autowired
    private NotificationRepositoryService repositoryService;

    @Autowired
    private NotificationService service;

    @BeforeEach
    void cleanUp() {
        repositoryService.deleteAll();
    }

    @Test
    public void createShouldSaveNotificationInDb() {
        service.create(NOTIFICATION);

        Notification inDb = repositoryService.findAll().get(0);

        assertEquals(NOTIFICATION, inDb);
    }

    @Test
    public void createShouldThrowIfNotificationAlreadyExists() {
        repositoryService.save(NOTIFICATION);

        Executable executable = () -> service.create(NOTIFICATION);

        assertThrows(NotificationAlreadyExistsException.class, executable);
    }

    @Test
    public void updateShouldPatchNotificationInDb() {
        repositoryService.save(NOTIFICATION);

        service.update(ALTERED_TEXT);

        Notification inDb = repositoryService.findAll().get(0);

        assertEquals(ALTERED_TEXT, inDb);
    }

    @Test
    public void updateShouldThrowIfNotificationDoesntExist() {
        Executable executable = () -> service.update(NOTIFICATION);

        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    public void deleteShouldRemoveNotificationFromDb() {
        repositoryService.save(NOTIFICATION);

        service.delete(NOTIFICATION);

        List<Notification> shouldBeEmpty = repositoryService.findAll();

        assertEquals(0, shouldBeEmpty.size());
    }

    @Test
    public void deleteThrowsIfNotificationDoesntExist() {
        Executable executable = () -> service.delete(NOTIFICATION);

        assertThrows(NotificationDoesntExistException.class, executable);
    }

    @Test
    public void clearShouldRemoveAllEntriesByAccountId() {
        repositoryService.save(NOTIFICATION);
        repositoryService.save(ALTERED_NAME);

        repositoryService.save(ALTERED_ACCOUNT_ID);

        service.clear(NOTIFICATION.getAccountId());

        List<Notification> expectedResult = Collections.singletonList(ALTERED_ACCOUNT_ID);
        List<Notification> trueResult = repositoryService.findAll();

        assertEquals(expectedResult, trueResult);
    }

    @Test
    public void changeAccountIdShouldChangeIds() {
        repositoryService.save(ALTERED_NAME);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        String oldAccountId = ALTERED_ACCOUNT_ID.getAccountId();
        String newAccountId = ALTERED_NAME.getAccountId();

        service.changeAccountId(oldAccountId, newAccountId);

        List<Notification> expectedResult = new ArrayList<>();
        expectedResult.add(ALTERED_NAME);
        expectedResult.add(NOTIFICATION);
        List<Notification> trueResult = repositoryService.findAll();

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }

    @Test
    public void findAllLikePrimaryKeyShouldGetAllLikePrimaryKey() {
        repositoryService.save(NOTIFICATION);
        repositoryService.save(ALTERED_NAME);
        repositoryService.save(LIKE_NAME);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        List<Notification> expectedResult = new ArrayList<>();
        expectedResult.add(NOTIFICATION);
        expectedResult.add(LIKE_NAME);
        List<Notification> trueResult = service.findAllLikePrimaryKey(NOTIFICATION.getAccountId(), NOTIFICATION.getName());

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }

    @Test
    public void findAllByAccountIdShouldGetAllByAccountId() {
        repositoryService.save(NOTIFICATION);
        repositoryService.save(ALTERED_NAME);
        repositoryService.save(LIKE_NAME);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        List<Notification> expectedResult = new ArrayList<>();
        expectedResult.add(NOTIFICATION);
        expectedResult.add(ALTERED_NAME);
        expectedResult.add(LIKE_NAME);
        List<Notification> trueResult = service.findAllByAccountId(NOTIFICATION.getAccountId());

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }
}
