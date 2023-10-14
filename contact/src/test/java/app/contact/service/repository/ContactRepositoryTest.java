package app.contact.service.repository;

import app.contact.model.Contact;
import app.contact.model.Method;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void findAllByUsername() {
        String username = "username";
        Contact contact1 = new Contact(username, Method.SMS, "380953766409", "notification");
        Contact contact2 = new Contact(username, Method.TELEGRAM, "380953766409", "notification");
        Contact contact3 = new Contact(username, Method.VIBER, "380953766409", "notification");
        Contact contact4 = new Contact(username, Method.EMAIL, "leskotr23@gmail.com");
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);
        contactRepository.saveAll(contacts);
        assertTrue(contacts.containsAll(contactRepository.findAllByUsername(username)));
        assertFalse(contactRepository.findAllByUsername("username1").containsAll(contacts));
    }
}