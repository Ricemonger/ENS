package app.contact;

import app.contact.controller.ContactController;
import app.contact.controller.ContactControllerService;
import app.contact.model.ContactService;
import app.contact.model.db.ContactRepository;
import app.contact.model.db.ContactRepositoryService;
import app.utils.feign_clients.security.SecurityJwtWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ContactServerTests {

    @Autowired
    private SecurityJwtWebClient securityJwtWebClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactRepositoryService contactRepositoryService;

    @Autowired
    private ContactControllerService contactControllerService;

    @Autowired
    private ContactController contactController;

    @Autowired
    private ContactService contactService;

    @Test
    void contextLoads() {
        assertNotNull(securityJwtWebClient);
        assertNotNull(contactRepository);
        assertNotNull(contactRepositoryService);
        assertNotNull(contactControllerService);
        assertNotNull(contactController);
        assertNotNull(contactService);
    }
}
