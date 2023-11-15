package app.contact.model;

import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.model.db.ContactRepositoryService;
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
public class ContactServiceTests {

    private final static Contact CONTACT = new Contact("9999", Method.SMS, "9999", "name");

    private final static Contact ALTERED_METHOD = new Contact("9999", Method.TELEGRAM, "9999", "name");

    private final static Contact ALTERED_CONTACT_ID = new Contact("9999", Method.SMS, "1111", "name");

    private final static Contact LIKE_CONTACT_ID = new Contact("9999", Method.SMS, "9999999", "name");

    private final static Contact ALTERED_NOTIFICATION_NAME = new Contact("9999", Method.SMS, "9999", "eman");

    private final static Contact ALTERED_ACCOUNT_ID = new Contact("1111", Method.SMS, "9999", "name");

    @Autowired
    private ContactRepositoryService repositoryService;

    @Autowired
    private ContactService service;

    @BeforeEach
    void cleanUp() {
        repositoryService.deleteAll();
    }

    @Test
    void createShouldSaveContactInDb() {
        service.create(CONTACT);

        Contact saved = repositoryService.findAll().get(0);

        assertEquals(CONTACT, saved);
    }

    @Test
    void createShouldThrowIfContactAlreadyExists() {
        repositoryService.save(CONTACT);

        Executable executable = () -> service.create(CONTACT);

        assertThrows(ContactAlreadyExistsException.class, executable);
    }

    @Test
    void updateShouldPatchContactInDb() {
        repositoryService.save(CONTACT);

        service.update(ALTERED_NOTIFICATION_NAME);

        Contact inDb = repositoryService.findAll().get(0);

        assertEquals(ALTERED_NOTIFICATION_NAME, inDb);
    }

    @Test
    void updateShouldThrowIfContactDoesntExist() {
        Executable executable = () -> service.update(CONTACT);

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    void deleteShouldRemoveContactFromDb() {
        repositoryService.save(CONTACT);

        service.delete(CONTACT);

        assertEquals(0, repositoryService.findAll().size());
    }

    @Test
    void deleteShouldThrowIfContactDoesntExist() {
        Executable executable = () -> service.delete(CONTACT);

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    void clearShouldRemoveAllEntriesByAccountId() {
        repositoryService.save(CONTACT);
        repositoryService.save(ALTERED_CONTACT_ID);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        service.clear(CONTACT.getAccountId());
        List<Contact> expectedResult = Collections.singletonList(ALTERED_ACCOUNT_ID);
        List<Contact> trueResult = repositoryService.findAll();

        assertEquals(expectedResult, trueResult);
    }

    @Test
    void changeAccountIdShouldChangeIds() {
        repositoryService.save(ALTERED_METHOD);
        repositoryService.save(ALTERED_CONTACT_ID);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        String oldAccountId = ALTERED_ACCOUNT_ID.getAccountId();
        String newAccountId = CONTACT.getAccountId();
        service.changeAccountId(oldAccountId, newAccountId);

        List<Contact> expectedResult = new ArrayList<>();
        expectedResult.add(ALTERED_METHOD);
        expectedResult.add(ALTERED_CONTACT_ID);
        expectedResult.add(CONTACT);
        List<Contact> trueResult = repositoryService.findAllByAccountId(newAccountId);

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }

    @Test
    void findAllLikePrimaryKeyShouldGetLikePK() {
        repositoryService.save(CONTACT);
        repositoryService.save(ALTERED_METHOD);
        repositoryService.save(ALTERED_CONTACT_ID);
        repositoryService.save(LIKE_CONTACT_ID);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        List<Contact> expectedResult = new ArrayList<>();
        expectedResult.add(CONTACT);
        expectedResult.add(LIKE_CONTACT_ID);
        List<Contact> trueResult = service.findAllLikePrimaryKey(CONTACT.getAccountId(), CONTACT.getMethod(),
                CONTACT.getContactId());

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }

    @Test
    void findAllLikeNotificationNameShouldGetByNN() {
        repositoryService.save(ALTERED_METHOD);
        repositoryService.save(ALTERED_CONTACT_ID);
        repositoryService.save(ALTERED_NOTIFICATION_NAME);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        List<Contact> expectedResult = new ArrayList<>();
        expectedResult.add(ALTERED_METHOD);
        expectedResult.add(ALTERED_CONTACT_ID);
        List<Contact> trueResult = service.findAllLikeNotificationName(CONTACT.getAccountId(), CONTACT.getNotificationName());

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }

    @Test
    void findAllByAccountIdShouldGetAllByAccountId() {
        repositoryService.save(CONTACT);
        repositoryService.save(ALTERED_METHOD);
        repositoryService.save(ALTERED_CONTACT_ID);
        repositoryService.save(ALTERED_ACCOUNT_ID);

        List<Contact> expectedResult = new ArrayList<>();
        expectedResult.add(CONTACT);
        expectedResult.add(ALTERED_METHOD);
        expectedResult.add(ALTERED_CONTACT_ID);
        List<Contact> trueResult = service.findAllByAccountId(CONTACT.getAccountId());

        assertTrue(expectedResult.containsAll(trueResult) && trueResult.containsAll(expectedResult));
    }
}
