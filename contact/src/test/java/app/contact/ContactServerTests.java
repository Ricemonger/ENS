package app.contact;

import app.contact.controller.ContactController;
import app.contact.service.ContactService;
import app.contact.service.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ContactServerTests {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactController contactController;

    @Test
    void contextLoads() throws Exception{
        assertNotNull(contactRepository);
        assertNotNull(contactService);
        assertNotNull(contactController);
    }

}
