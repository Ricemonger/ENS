package app.contact.model.db;

import app.contact.exceptions.ContactDoesntExistException;
import app.contact.model.Contact;
import app.contact.model.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactRepositoryServiceTests {

    private final static ContactEntity ENTITY = new ContactEntity("accountId", Method.SMS, "contactId", "notificationName");

    private final static Contact CONTACT = new Contact("accountId", Method.SMS, "contactId", "notificationName");

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactRepositoryService service;

    @Test
    public void save() {
        service.save(CONTACT);

        verify(repository).save(ENTITY);
    }

    @Test
    public void findByIdOrThrow() {
        Executable executable = () -> service.findByIdOrThrow(CONTACT.getAccountId(), CONTACT.getMethod(),
                CONTACT.getContactId());

        assertThrows(ContactDoesntExistException.class, executable);
    }

    @Test
    public void findAllByAccountId() {
        service.findAllByAccountId(CONTACT.getAccountId());

        verify(repository).findAllByAccountId(CONTACT.getAccountId());
    }

    @Test
    public void findAll() {
        service.findAll();

        verify(repository).findAll();
    }

    @Test
    public void delete() {
        service.delete(CONTACT);

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

        service.deleteAll(Collections.singletonList(CONTACT));

        verify(repository).deleteAll(Collections.singletonList(ENTITY));
    }
}
