package app.contact.service.repository;

import app.contact.service.Contact;
import app.contact.service.Method;
import app.contact.service.db.ContactRepository;
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
    void findAllByAccountId() {
        String accountId = "username";
        Contact contact1 = new Contact(accountId, Method.SMS, "380953766409", "notification");
        Contact contact2 = new Contact(accountId, Method.TELEGRAM, "380953766409", "notification");
        Contact contact3 = new Contact(accountId, Method.VIBER, "380953766409", "notification");
        Contact contact4 = new Contact(accountId, Method.EMAIL, "leskotr23@gmail.com");
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);
        contactRepository.saveAll(contacts);
        assertTrue(contacts.containsAll(contactRepository.findAllByAccountId(accountId)));
        assertFalse(contactRepository.findAllByAccountId("username1").containsAll(contacts));
    }
}