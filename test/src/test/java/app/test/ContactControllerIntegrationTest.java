package app.test;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.exceptions.ContactAlreadyExistsException;
import app.contact.model.Contact;
import app.contact.model.ContactCompositeKey;
import app.contact.model.Method;
import app.contact.service.repository.ContactRepository;
import app.notification.service.repository.NotificationRepository;
import app.security.user.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableJpaRepositories({"app.test"
        ,"app.contact.model","app.contact.service.repository"
        ,"app.notification.model","app.notification.service.repository"
        ,"app.security.user.model","app.security.user.service.repository"})
@ComponentScan(basePackages = {"app.test"
        ,"app.contact.model","app.contact.service.repository"
        ,"app.notification.model","app.notification.service.repository"
        ,"app.security.user.model","app.security.user.service.repository"})
@EntityScan({
        "app.contact.model","app.contact.service.repository"
        ,"app.notification.model","app.notification.service.repository"
        ,"app.security.user.model","app.security.user.service.repository"})
@EnableConfigurationProperties
public class ContactControllerIntegrationTest {

    private WebClient mainUserWebClient;;

    private WebClient anotherUserWebClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final ContactCreUpdRequest request = new ContactCreUpdRequest("sms","380","name");
    private static final ContactCreUpdRequest updateRequest = new ContactCreUpdRequest("sms","380","new notification name");
    private static final ContactCreUpdRequest anotherRequest = new ContactCreUpdRequest("SMS","38095","name");
    private static final ContactCreUpdRequest wrongRequest = new ContactCreUpdRequest("smmmmmms","380","name");
    private static final ContactKeyRequest wrongDeleteRequest = new ContactKeyRequest("smmmmmms","380");
    private static final ContactKeyRequest deleteRequest = new ContactKeyRequest("sms","380");

    @BeforeEach
    void beforeEach(){
        userRepository.deleteAll();
        contactRepository.deleteAll();
        notificationRepository.deleteAll();
        String username = "username";
        String password = "password";
        String token = WebClient.builder().baseUrl("http://localhost:8080/api/users/register").build().post().bodyValue(new User(username, password)).retrieve().bodyToMono(String.class).block();
        mainUserWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/contacts")
                .defaultHeader("Authorization","Bearer " + token)
                .build();
        String username1 = "username1";
        String password1 = "password1";
        String token1 = WebClient.builder().baseUrl("http://localhost:8080/api/users/register").build().post().bodyValue(new User(username1, password1)).retrieve().bodyToMono(String.class).block();
        anotherUserWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/contacts")
                .defaultHeader("Authorization","Bearer " + token1)
                .build();
    }

    @Test
    void createNormalBehavior(){
        Contact firstUserRequest = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherRequest = mainUserWebClient.post().bodyValue(anotherRequest).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserRequest = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(firstUserRequest);
        contacts.add(firstUserAnotherRequest);

        assertEquals(firstUserRequest,contactRepository.findById(new ContactCompositeKey("username", Method.SMS,"380")).orElseThrow());

        assertEquals(contacts, Set.copyOf(contactRepository.findAllByUsername("username")));

        contacts.add(anotherUserRequest);
        assertEquals(contacts,Set.copyOf(contactRepository.findAll()));
    }

    @Test
    void createThrowsExceptionsOnWrongMethodOrAlreadyExisting() {
        mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Executable alreadyExistsExecutable = () -> mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.Forbidden.class,alreadyExistsExecutable);

        Executable wrongMethodExecutable = () -> mainUserWebClient.post().bodyValue(wrongRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class,wrongMethodExecutable);
    }

    @Test
    void updateNormalBehavior(){
        Contact initial = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUser = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact updated = mainUserWebClient.patch().bodyValue(updateRequest).retrieve().bodyToMono(Contact.class).block();
        Contact inDb = contactRepository.findById(new ContactCompositeKey("username",Method.SMS,request.contactId())).orElseThrow();
        Contact anotherUserInDb = contactRepository.findById(new ContactCompositeKey("username1",Method.SMS,request.contactId())).orElseThrow();

        assertNotEquals(initial,inDb);
        assertEquals(updated,inDb);
        assertEquals(anotherUser,anotherUserInDb);
    }

    @Test
    void updateThrowsExceptionsOnWrongMethodOrNotExisting(){
        Executable notExistExecutable = () -> mainUserWebClient.patch().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class,notExistExecutable);

        Executable wrongMethodExecutable = () -> mainUserWebClient.patch().bodyValue(wrongRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class,wrongMethodExecutable);
    }

    @Test
    void deleteNormalBehavior(){
        Contact initial = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUser = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact deleted = mainUserWebClient.method(HttpMethod.DELETE).bodyValue(deleteRequest).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserInDb = contactRepository.findById(new ContactCompositeKey("username1",Method.SMS,request.contactId())).orElseThrow();

        assertEquals(new Contact(initial.getUsername(),initial.getMethod(),initial.getContactId()),deleted);
        Executable executable = () ->contactRepository.findById(new ContactCompositeKey("username",Method.SMS,request.contactId())).orElseThrow();
        assertThrows(NoSuchElementException.class,executable);
        assertEquals(anotherUser,anotherUserInDb);
    }

    @Test
    void deleteThrowsExceptionsOnWrongMethodOrNotExisting(){
        Executable wrongMethodExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(wrongDeleteRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class,wrongMethodExecutable);

        Executable notExistExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(deleteRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class,notExistExecutable);
    }

    record User(String username, String password){}
}
