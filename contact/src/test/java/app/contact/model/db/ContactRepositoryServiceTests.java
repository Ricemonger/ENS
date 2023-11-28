package app.contact.model.db;

import app.contact.model.Contact;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.contact.exceptions.ContactDoesntExistException;
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
public class ContactRepositoryServiceTests {

    private final static String ACCOUNT_ID = "1111";

    private final static String ANOTHER_ACCOUNT_ID = "9999";

    private final static Method METHOD = Method.SMS;

    private final static String CONTACT_ID = "contactId";

    private final static String NOTIFICATION = "notificationName";

    private final static Contact CONTACT = new Contact(ACCOUNT_ID, METHOD, CONTACT_ID, NOTIFICATION);

    private final static ContactEntity ENTITY = new ContactEntity(ACCOUNT_ID, METHOD, CONTACT_ID, NOTIFICATION);

    private final static ContactEntity ANOTHER_ENTITY = new ContactEntity(ANOTHER_ACCOUNT_ID, METHOD, CONTACT_ID, NOTIFICATION);

    @SpyBean
    private ContactRepository repository;

    @Autowired
    private ContactRepositoryService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void save() {
        service.save(CONTACT);

        verify(repository).save(ENTITY);

        assertEquals(new Contact(repository.findAll().get(0)), CONTACT);
    }

    @Test
    public void doesContactExistsShouldReturnTrueIfExists() {
        repository.save(ENTITY);

        assertTrue(service.doesContactExists(CONTACT));
    }

    @Test
    public void doesContactExistsShouldReturnFalseIfDoesntExist() {
        repository.save(ANOTHER_ENTITY);

        assertFalse(service.doesContactExists(CONTACT));
    }

    @Test
    public void findByIdOrThrow() {
        repository.save(ENTITY);

        Contact contact = service.findByIdOrThrow(ACCOUNT_ID, METHOD, CONTACT_ID);

        assertEquals(new Contact(repository.findAll().get(0)), contact);
    }

    @Test
    public void findByIdOrThrowShouldThrowIfDoesntExist() {
        Executable executable = () -> service.findByIdOrThrow(ACCOUNT_ID, METHOD, CONTACT_ID);

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    public void findAllByAccountId() {
        Contact expected = new Contact(repository.save(ENTITY));
        repository.save(ANOTHER_ENTITY);

        List<Contact> expectedResult = Collections.singletonList(expected);

        List<Contact> trueResult = service.findAllByAccountId(ACCOUNT_ID);

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

        service.delete(CONTACT);

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

        service.deleteAll(Collections.singletonList(CONTACT));

        verify(repository).deleteAll(Collections.singletonList(ENTITY));
    }
}
