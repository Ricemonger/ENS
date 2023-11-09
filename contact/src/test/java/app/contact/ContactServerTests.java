package app.contact;

import app.contact.controller.ContactController;
import app.contact.model.db.ContactRepository;
import app.contact.model.db.ContactRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ContactServerTests {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactRepositoryService contactRepositoryService;

    @Autowired
    private ContactController contactController;

    @Test
    void contextLoads() throws Exception {
        assertNotNull(contactRepository);
        assertNotNull(contactRepositoryService);
        assertNotNull(contactController);
    }

}
