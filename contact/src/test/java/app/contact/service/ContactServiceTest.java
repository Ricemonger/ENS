package app.contact.service;

import app.contact.controller.exceptions.ContactDoesntExistException;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.service.repository.ContactRepository;
import org.junit.jupiter.api.AfterEach;
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
class ContactServiceTest {

    @Autowired
    private ContactRepository contactRepository;
    private ContactService contactService;

    @BeforeEach
    void setUp(){
        contactService = new ContactService(contactRepository);
    }
    @AfterEach
    void clearDB(){
        contactRepository.deleteAll();
    }

    @Test
    void createNormalBehavior() {
        Contact contact = new Contact("username", Method.SMS,"380953766409","");
        contactService.create(contact);
    }

    @Test
    void updateThrowsExceptionIfNotExists() {
        Contact contact = new Contact("username", Method.SMS,"380953766409","");
        Executable executable = new Executable(){
            @Override
            public void execute() throws Throwable {
                contactService.update(contact);
            }
        };
        assertThrows(ContactDoesntExistException.class,executable);
    }
    @Test
    void updateNormalBehavior() {
        Contact contact = new Contact("username", Method.SMS,"380953766409","");
        contactRepository.save(contact);
        Executable executable = new Executable(){
            @Override
            public void execute() throws Throwable {
                contactService.update(contact);
            }
        };
        assertDoesNotThrow(executable);
    }


    @Test
    void deleteThrowsExceptionIfNotExists() {
        Contact contact = new Contact("username", Method.SMS,"380953766409","");
        Executable executable = new Executable(){
            @Override
            public void execute() throws Throwable {
                contactService.delete(contact);
            }
        };
        assertThrows(ContactDoesntExistException.class,executable);
    }
    @Test
    void deleteNormalBehavior() {
        Contact contact = new Contact("username", Method.SMS,"380953766409","");
        contactRepository.save(contact);
        Executable executable = new Executable(){
            @Override
            public void execute() throws Throwable {
                contactService.delete(contact);
            }
        };
        assertDoesNotThrow(executable);
    }

    @Test
    void findAllByUsername() {
        String username = "username";
        String invalidUsername = "";
        Contact contact1 = new Contact(username, Method.SMS,"380953766409","notification");
        Contact contact2 = new Contact(username, Method.TELEGRAM,"380953766409","notification");
        Contact contact3 = new Contact(username, Method.VIBER,"380953766409","notification");
        Contact contact4 = new Contact(username, Method.EMAIL,"leskotr23@gmail.com");
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);
        contactRepository.saveAll(contacts);
        assertTrue(contactService.findAllByUsername(username).containsAll(contacts));
        assertFalse(contactService.findAllByUsername(invalidUsername).containsAll(contacts));
    }

    @Test
    void findAllLikePrimaryKey() {
        String username = "username";
        Contact contact1 = new Contact(username, Method.SMS,"380953766409","");
        Contact contact2 = new Contact(username, Method.SMS,"380953","notifation");
        Contact contact3 = new Contact(username, Method.SMS,"38","tion");
        Contact contact4 = new Contact(username, Method.EMAIL,"38095375590");
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);
        contactRepository.saveAll(contacts);
        List<Contact> result = new ArrayList<>();
        result.add(contact1);
        result.add(contact2);
        assertTrue(contactService.findAllLikePrimaryKey(username, Method.SMS, "380").containsAll(result));
        assertFalse(contactService.findAllLikePrimaryKey(username, Method.SMS, "380").contains(contact3));
        assertFalse(contactService.findAllLikePrimaryKey(username, Method.SMS, "380").contains(contact4));
    }

    @Test
    void findOneByPrimaryKeyThrowsExceptionIfNotExists() {
        Contact contact1 = new Contact("username", Method.SMS, "380953");
        Contact contact2 = new Contact("username", Method.VIBER, "380953766409");
        contactRepository.save(contact1);
        contactRepository.save(contact2);
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                contactService.findOneByPrimaryKey("username", Method.SMS, "380953766409");
            }
        };
        assertThrows(ContactDoesntExistException.class,executable);
    }
    @Test
    void findOneByPrimaryKeyNormalBehavior() {
        Contact contact = new Contact("username", Method.SMS, "380953766409");
        contactRepository.save(contact);
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                contactService.findOneByPrimaryKey("username", Method.SMS, "380953766409");
            }
        };
        assertDoesNotThrow(executable);
        assertEquals(contactService.findOneByPrimaryKey("username", Method.SMS, "380953766409"), contact);
    }

    @Test
    void findAllLikeNotificationName() {
        Contact contact1 = new Contact("username", Method.SMS, "380953766409","");
        Contact contact2 = new Contact("", Method.SMS, "380953766409","");
        contactRepository.save(contact1);
        contactRepository.save(contact2);
        assertEquals(contactService.findAllLikeNotificationName("","1"), Collections.emptyList());
        Contact contact3 = new Contact("username", Method.SMS, "380953","12787676");
        contactRepository.save(contact3);
        assertEquals(contactService.findAllLikeNotificationName("username","1"), Collections.singletonList(contact3));
    }
}