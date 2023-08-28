package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.service.ContactService;
import app.contact.service.repository.ContactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utils.JwtClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ContractControllerTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactService contactService = new ContactService(contactRepository);

    @Mock
    private JwtClient jwtUtil = new JwtClient();

    private ContractController contractController = new ContractController(contactService,jwtUtil);

    @AfterEach
    void clearDB(){
        contactRepository.deleteAll();
    }

    @Test
    void create() {
        Contact contact = new Contact("", Method.SMS,"380953766409");
        ContactCreUpdRequest request = new ContactCreUpdRequest("SMS","380953766409","");
        contractController.create("",request);
        verify(contactService).create(contact);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAllByUsername() {
    }

    @Test
    void findAllLikePrimaryKey() {
    }

    @Test
    void findAllLikeNotificationName() {
    }

    @Test
    void contactDoesntExistException() {
    }

    @Test
    void contactAlreadyExistsException() {
    }

    @Test
    void invalidContactMethodException() {
    }

    @Test
    void jwtRuntimeException() {
    }

    @Test
    void unknownException() {
    }
}