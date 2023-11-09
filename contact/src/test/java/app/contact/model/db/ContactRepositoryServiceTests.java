package app.contact.model.db;

import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.model.Contact;
import app.contact.model.Method;
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
public class ContactRepositoryServiceTests {

    private final Contact CONTACT = new Contact("9999", Method.SMS, "9999", "name");

    private final Contact ALTERED_NOTIFICATION_NAME = new Contact("9999", Method.SMS, "9999", "eman");

    private final Contact ALTERED_CONTACT_ID = new Contact("9999", Method.SMS, "1111", "name");

    private final Contact ANOTHER_ACCOUNT_ID_CONTACT_ID = new Contact("1111", Method.SMS, "1111", "name");

    @Autowired
    private ContactRepository repository;

    @Autowired
    private ContactRepositoryService service;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void createShouldSaveContactInDb() {
        service.create(CONTACT);

        Contact saved = repository.findAll().get(0);

        assertEquals(CONTACT, saved);
    }

    @Test
    void createShouldThrowIfUserExists() {
        repository.save(CONTACT);

        Executable executable = () -> service.create(CONTACT);

        assertThrows(ContactAlreadyExistsException.class, executable);
    }

    @Test
    void updateShouldPatchContactInDb() {
        repository.save(CONTACT);

        service.update(ALTERED_NOTIFICATION_NAME);

        Contact inDb = repository.findAll().get(0);

        assertEquals(ALTERED_NOTIFICATION_NAME, inDb);
    }

    @Test
    void updateShouldThrowIfUserDoesntExist() {
        Executable executable = () -> service.update(CONTACT);

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    void deleteShouldRemoveFromDb() {
        repository.save(CONTACT);

        service.delete(CONTACT);

        assertEquals(0, repository.findAll().size());
    }

    @Test
    void deleteShouldThrowIfUserDoesntExist() {
        Executable executable = () -> service.delete(CONTACT);

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    void clearShouldRemoveAllEntriesByAccountId() {
        repository.save(CONTACT);
        repository.save(ALTERED_CONTACT_ID);
        repository.save(ANOTHER_ACCOUNT_ID_CONTACT_ID);

        String accountId = CONTACT.getAccountId();
        service.clear(accountId);

        assertEquals(repository.findAll(), Collections.singletonList(ANOTHER_ACCOUNT_ID_CONTACT_ID));
    }

    @Test
    void changeAccountIdShouldChangeIds() {
        repository.save(ALTERED_CONTACT_ID);

        String oldAccountId = ALTERED_CONTACT_ID.getAccountId();
        String newAccountId = ANOTHER_ACCOUNT_ID_CONTACT_ID.getAccountId();

        service.changeAccountId(oldAccountId, newAccountId);
        Contact inDb = repository.findAll().get(0);

        assertEquals(inDb, ANOTHER_ACCOUNT_ID_CONTACT_ID);
    }

    @Test
    void findAllLikePrimaryKeyShouldGetLikePK() {
        repository.save(CONTACT);
        repository.save(ALTERED_CONTACT_ID);
        repository.save(ANOTHER_ACCOUNT_ID_CONTACT_ID);

        String accountIdFilter = ALTERED_CONTACT_ID.getAccountId();
        Method methodFilter = ALTERED_CONTACT_ID.getMethod();
        String contactIdFilter = "1";

        List<Contact> found = service.findAllLikePrimaryKey(accountIdFilter, methodFilter, contactIdFilter);

        assertEquals(found, Collections.singletonList(ALTERED_CONTACT_ID));
    }

    @Test
    void findAllLikeNotificationNameShouldGetByNN() {
        repository.save(ALTERED_NOTIFICATION_NAME);
        repository.save(ALTERED_CONTACT_ID);
        repository.save(ANOTHER_ACCOUNT_ID_CONTACT_ID);

        String accountIdFilter = ALTERED_CONTACT_ID.getAccountId();
        String notificationNameFilter = ALTERED_CONTACT_ID.getNotificationName();

        List<Contact> found = service.findAllLikeNotificationName(accountIdFilter, notificationNameFilter);

        assertEquals(found, Collections.singletonList(ALTERED_CONTACT_ID));
    }

    @Test
    void findAllByAccountIdShouldGetByAccountId() {
        repository.save(ALTERED_NOTIFICATION_NAME);
        repository.save(ALTERED_CONTACT_ID);
        repository.save(ANOTHER_ACCOUNT_ID_CONTACT_ID);

        String accountIdFilter = ALTERED_CONTACT_ID.getAccountId();
        List<Contact> trueResult = new ArrayList<>();
        trueResult.add(ALTERED_NOTIFICATION_NAME);
        trueResult.add(ALTERED_CONTACT_ID);

        List<Contact> found = service.findAllByAccountId(accountIdFilter);

        assertTrue(trueResult.containsAll(found) && found.containsAll(trueResult));
    }
}
