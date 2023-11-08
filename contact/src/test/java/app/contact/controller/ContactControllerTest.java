package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.service.Contact;
import app.contact.service.Method;
import app.contact.service.db.ContactCompositeKey;
import app.contact.service.db.ContactRepository;
import app.contact.service.db.ContactRepositoryService;
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
class ContactControllerTest {
    @Autowired
    private ContactRepository contactRepository;

    private ContactRepositoryService contactRepositoryService;

    @Mock
    private JwtClient jwtClient;

    private ContactController contactController;

    @Mock
    private ContactRepositoryService mockContactRepositoryService;

    @BeforeEach
    void setUp() {
        contactRepositoryService = new ContactRepositoryService(contactRepository);
        contactController = new ContactController(contactRepositoryService, jwtClient);
    }

    @Test
    void create() {
        String token = "token";
        ContactCreUpdRequest request = new ContactCreUpdRequest(Method.SMS.name(), "380", "name");
        contactController.create(token, request);
        verify(jwtClient).extractAccountId(token);
        Contact contact1 = new Contact(null, Method.SMS, request.contactId(), request.notificationName());
        assertEquals(contact1, contactRepository.findById(new ContactCompositeKey(null, Method.SMS, request.contactId())).orElseThrow());
        Executable executable = () -> contactController.create(token, request);
        assertThrows(ContactAlreadyExistsException.class, executable);
    }

    @Test
    void update() {
        String token = "token";
        ContactCreUpdRequest request = new ContactCreUpdRequest(Method.SMS.name(), "380", "name");
        Executable executable = () -> contactController.update(token, request);
        assertThrows(ContactDoesntExistException.class, executable);
        contactController.create(token, request);
        ContactCreUpdRequest anotherRequest = new ContactCreUpdRequest(Method.SMS.name(), "380", "anotherName");
        Contact anotherContact = new Contact(null, Method.SMS, "380", "anotherName");
        reset(jwtClient);
        contactController.update(token, anotherRequest);
        verify(jwtClient).extractAccountId(token);
        assertEquals(contactRepository.findById(new ContactCompositeKey(null, Method.SMS, "380")).orElseThrow().getNotificationName(), anotherContact.getNotificationName());
    }

    @Test
    void delete() {
        String token = "token";
        ContactKeyRequest request = new ContactKeyRequest(Method.SMS.name(), "380");
        Executable executable = () -> contactController.delete(token, request);
        assertThrows(ContactDoesntExistException.class, executable);
        contactRepository.save(new Contact(null, Method.SMS, request.contactId(), "name"));
        contactController.delete(token, request);
        Executable executableAfter = () -> contactRepository.findById(new ContactCompositeKey(null, Method.SMS, request.contactId())).orElseThrow();
        assertThrows(NoSuchElementException.class, executableAfter);
    }

    @Test
    void findAllByAccountId() {
        contactController = new ContactController(mockContactRepositoryService, jwtClient);
        contactController.findAllByAccountId("");
        verify(mockContactRepositoryService).findAllByAccountId(any());
    }

    @Test
    void findAllLikePrimaryKey() {
        contactController = new ContactController(mockContactRepositoryService, jwtClient);
        contactController.findAllLikePrimaryKey("", new ContactKeyRequest(Method.SMS.name(), "380"));
        verify(mockContactRepositoryService).findAllLikePrimaryKey(any(), eq(Method.SMS), eq("380"));
    }

    @Test
    void findAllLikeNotificationName() {
        contactController = new ContactController(mockContactRepositoryService, jwtClient);
        String name = "name";
        contactController.findAllLikeNotificationName("", new ContactNNRequest(name));
        verify(mockContactRepositoryService).findAllLikeNotificationName(any(), eq(name));
    }
}