package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.controller.exceptions.ContactAlreadyExistsException;
import app.contact.controller.exceptions.ContactDoesntExistException;
import app.contact.model.Contact;
import app.contact.model.ContactCompositeKey;
import app.contact.model.Method;
import app.contact.service.ContactService;
import app.contact.service.repository.ContactRepository;
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

    private ContactService contactService;

    @Mock
    private JwtClient jwtClient;

    private ContactController contactController;

    @Mock
    private ContactService mockContactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService(contactRepository);
        contactController = new ContactController(contactService, jwtClient);
    }

    @Test
    void create() {
        String token = "token";
        ContactCreUpdRequest request = new ContactCreUpdRequest(Method.SMS.name(), "380", "name");
        contactController.create(token, request);
        verify(jwtClient).extractUsername(token);
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
        verify(jwtClient).extractUsername(token);
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
    void findAllByUsername() {
        contactController = new ContactController(mockContactService, jwtClient);
        contactController.findAllByUsername("");
        verify(mockContactService).findAllByUsername(any());
    }

    @Test
    void findAllLikePrimaryKey() {
        contactController = new ContactController(mockContactService, jwtClient);
        contactController.findAllLikePrimaryKey("", new ContactKeyRequest(Method.SMS.name(), "380"));
        verify(mockContactService).findAllLikePrimaryKey(any(), eq(Method.SMS), eq("380"));
    }

    @Test
    void findAllLikeNotificationName() {
        contactController = new ContactController(mockContactService, jwtClient);
        String name = "name";
        contactController.findAllLikeNotificationName("", new ContactNNRequest(name));
        verify(mockContactService).findAllLikeNotificationName(any(), eq(name));
    }
}