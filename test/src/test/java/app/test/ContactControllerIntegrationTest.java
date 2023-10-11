package app.test;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.model.Contact;
import app.contact.model.ContactCompositeKey;
import app.contact.model.Method;
import app.contact.service.repository.ContactRepository;
import app.notification.service.repository.NotificationRepository;
import app.security.user.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
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

    private WebClient mainUserWebClient;


    private WebClient anotherUserWebClient;


    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final ContactCreUpdRequest request = new ContactCreUpdRequest("sms","380","name");
    private static final ContactCreUpdRequest updateRequest = new ContactCreUpdRequest("sms","380","new notification name");
    private static final ContactCreUpdRequest anotherRequest = new ContactCreUpdRequest("SMS","11000","name");
    private static final ContactCreUpdRequest anotherNotificationRequest = new ContactCreUpdRequest("SMS","8665","another notification");
    private static final ContactCreUpdRequest wrongRequest = new ContactCreUpdRequest("smmmmmms","380","name");
    private static final ContactNNRequest notificationNameRequest = new ContactNNRequest("name");
    private static final ContactNNRequest notificationNameRequestBlank = new ContactNNRequest("");

    @BeforeEach
    void beforeEach(){
        userRepository.deleteAll();
        contactRepository.deleteAll();
        notificationRepository.deleteAll();

        String username = "username";
        String password = "password";
        String token = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/users/register")
                .build()
                .post()
                .bodyValue(new User(username, password))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        mainUserWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/contacts")
                .defaultHeader("Authorization","Bearer " + token)
                .build();

        String username1 = "username1";
        String password1 = "password1";
        String token1 = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/users/register")
                .build()
                .post()
                .bodyValue(new User(username1, password1))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        anotherUserWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/contacts")
                .defaultHeader("Authorization","Bearer " + token1)
                .build();
    }

    @Test
    void createNormalBehavior(){
        Contact firstUserContact = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(anotherRequest).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(firstUserContact);
        contacts.add(firstUserAnotherContact);

        assertEquals(firstUserContact,contactRepository.findById(new ContactCompositeKey("username", Method.SMS,"380")).orElseThrow());

        assertEquals(contacts, Set.copyOf(contactRepository.findAllByUsername("username")));

        contacts.add(anotherUserContact);
        assertEquals(contacts,Set.copyOf(contactRepository.findAll()));
    }

    @Test
    void createThrowsExceptionsOnWrongMethod() {
        Executable wrongMethodExecutable = () -> mainUserWebClient.post().bodyValue(wrongRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class,wrongMethodExecutable);
    }

    @Test
    void createThrowsExceptionsOnAlreadyExisting() {
        mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Executable alreadyExistsExecutable = () -> mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.Forbidden.class, alreadyExistsExecutable);
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
    void updateThrowsExceptionsOnWrongMethod(){
        Executable wrongMethodExecutable = () -> mainUserWebClient.patch().bodyValue(wrongRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class,wrongMethodExecutable);
    }

    @Test
    void updateThrowsExceptionsOnNotExisting() {
        Executable notExistExecutable = () -> mainUserWebClient.patch().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class, notExistExecutable);
    }

    @Test
    void deleteNormalBehavior(){
        Contact initial = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUser = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact deleted = mainUserWebClient.method(HttpMethod.DELETE).bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserInDb = contactRepository.findById(new ContactCompositeKey("username1",Method.SMS,request.contactId())).orElseThrow();
        Executable executable = () ->contactRepository.findById(new ContactCompositeKey("username",Method.SMS,request.contactId())).orElseThrow();

        assertEquals(new Contact(initial.getUsername(),initial.getMethod(),initial.getContactId()),deleted);
        assertThrows(NoSuchElementException.class,executable);
        assertEquals(anotherUser,anotherUserInDb);
    }

    @Test
    void deleteThrowsExceptionsOnWrongMethod() {
        Executable wrongMethodExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(wrongRequest).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, wrongMethodExecutable);
    }

    @Test
    void deleteThrowsExceptionsOnNotExisting(){
        Executable notExistExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class,notExistExecutable);
    }

    @Test
    void findAllByUsernameNormalBehavior(){
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block(),Collections.emptyList());

        Contact firstUserContact = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(anotherRequest).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        List<Contact> contacts = new ArrayList<>();
        contacts.add(firstUserContact);
        contacts.add(firstUserAnotherContact);
        assertEquals(toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()),contacts);
        assertFalse(toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()).contains(anotherUserContact));
    }

    @Test
    void findAllLikePrimaryKeyNormalBehavior(){
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(request).retrieve().bodyToMono(List.class).block(),Collections.emptyList());

        List<Contact> contacts = new ArrayList<>();
        Contact firstUserContact = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(anotherRequest).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        ContactKeyRequest resultingOneRequest = new ContactKeyRequest(Method.SMS.name(),"38");
        ContactKeyRequest resultingTwoRequest = new ContactKeyRequest(Method.SMS.name(),"");

        contacts.add(firstUserContact);
        assertEquals(contacts,toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingOneRequest).retrieve().bodyToMono(List.class).block()));

        contacts.add(firstUserAnotherContact);
        assertEquals(contacts,toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block()));

        assertEquals(Collections.singletonList(anotherUserContact),toContactList(anotherUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block()));
    }

    @Test
    void findAllLikeNotificationNameNormalBehavior(){
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(request).retrieve().bodyToMono(List.class).block(),Collections.emptyList());

        Contact firstUserContact = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(anotherRequest).retrieve().bodyToMono(Contact.class).block();
        List<Contact> mainUserContacts = new ArrayList<>();
        mainUserContacts.add(firstUserContact);
        mainUserContacts.add(firstUserAnotherContact);
        Contact firstUserAnotherNotificationContact = mainUserWebClient.post().bodyValue(anotherNotificationRequest).retrieve().bodyToMono(Contact.class).block();
        List<Contact> returnedMainUserListName = toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(notificationNameRequest).retrieve().bodyToMono(List.class).block());
        List<Contact> returnedMainUserListAll = toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(notificationNameRequestBlank).retrieve().bodyToMono(List.class).block());

        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        List<Contact> returnedAnotherUserList = toContactList(anotherUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(notificationNameRequest).retrieve().bodyToMono(List.class).block());


        assertEquals(Collections.singletonList(anotherUserContact),returnedAnotherUserList);
        assertFalse(returnedMainUserListAll.contains(anotherUserContact));
        assertTrue(returnedMainUserListName.equals(mainUserContacts) && !returnedMainUserListName.contains(firstUserAnotherNotificationContact));
        mainUserContacts.add(firstUserAnotherNotificationContact);
        assertEquals(mainUserContacts,returnedMainUserListAll);
    }

    private List<Contact> toContactList(List<LinkedHashMap<String,String>> list){
        List<Contact> contacts = new ArrayList<>();
        for(LinkedHashMap<String,String> m : list){
            Contact contact = new Contact(m.get("username"),Method.valueOf(m.get("method")),m.get("contactId"),m.get("notificationName"));
            contacts.add(contact);
        }
        return contacts;
    }

    record User(String username, String password){}
}
