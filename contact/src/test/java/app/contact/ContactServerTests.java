package app.contact;

import app.contact.controller.ContactController;
import app.contact.controller.ContactControllerService;
import app.contact.model.ContactService;
import app.contact.model.db.ContactRepository;
import app.contact.model.db.ContactRepositoryService;
import app.utils.logger.AroundLogger;
import app.utils.services.security.abstact.feign.SecurityFeignClient;
import app.utils.services.security.abstact.feign.SecurityFeignClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ContactServerTests {

    @Autowired
    private AroundLogger aroundLogger;

    @Autowired
    private SecurityFeignClient securityFeignClient;

    @Autowired
    private SecurityFeignClientService securityFeignClientService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactRepositoryService contactRepositoryService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactControllerService contactControllerService;

    @Autowired
    private ContactController contactController;

    @Test
    void contextLoads() {
        assertNotNull(aroundLogger);
        assertNotNull(securityFeignClient);
        assertNotNull(securityFeignClientService);
        assertNotNull(contactRepository);
        assertNotNull(contactRepositoryService);
        assertNotNull(contactService);
        assertNotNull(contactControllerService);
        assertNotNull(contactController);
    }
}
