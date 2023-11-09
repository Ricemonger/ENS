package app.test;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.model.db.ContactCompositeKey;
import app.contact.model.db.ContactRepository;
import app.notification.model.db.NotificationRepository;
import app.security.ens_users.db.EnsUserRepository;
import io.jsonwebtoken.JwtException;
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
        , "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.ens_user.db"})
@ComponentScan(basePackages = {"app.test"
        , "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.ens_user.db"})
@EntityScan({
        "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.repository"})
@EnableConfigurationProperties
public class ContactControllerIntegrationTest {

    private WebClient mainUserWebClient;

    private WebClient anotherUserWebClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EnsUserRepository ensUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final ContactCreUpdRequest REQUEST = new ContactCreUpdRequest("sms", "380", "name");
    private static final ContactCreUpdRequest UPDATE_REQUEST = new ContactCreUpdRequest("sms", "380", "new notification name");
    private static final ContactCreUpdRequest ANOTHER_REQUEST = new ContactCreUpdRequest("SMS", "11000", "name");
    private static final ContactCreUpdRequest ANOTHER_NOTIFICATION_REQUEST = new ContactCreUpdRequest("SMS", "8665", "another notification");
    private static final ContactCreUpdRequest WRONG_REQUEST = new ContactCreUpdRequest("smmmmmms", "380", "name");
    private static final ContactNNRequest NOTIFICATION_NAME_REQUEST = new ContactNNRequest("name");
    private static final ContactNNRequest NOTIFICATION_NAME_REQUEST_BLANK = new ContactNNRequest("");

    private static final String FIRST_USER_NAME = "username";
    private static final String ANOTHER_USER_NAME = "username1";

    private final InvalidJwts invalidJwts = new InvalidJwts("http://localhost:8080/api/contacts");

    @BeforeEach
    void beforeEach() {
        ensUserRepository.deleteAll();
        contactRepository.deleteAll();
        notificationRepository.deleteAll();

        String username = FIRST_USER_NAME;
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
                .defaultHeader("Authorization", "Bearer " + token)
                .build();

        String username1 = ANOTHER_USER_NAME;
        String password1 = "password";
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
                .defaultHeader("Authorization", "Bearer " + token1)
                .build();
    }

    @Test
    void createNormalBehavior() {
        Contact firstUserContact = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(firstUserContact);
        contacts.add(firstUserAnotherContact);

        assertEquals(firstUserContact, contactRepository.findById(new ContactCompositeKey(FIRST_USER_NAME, Method.SMS, "380")).orElseThrow());

        assertEquals(contacts, Set.copyOf(contactRepository.findAllByAccountId(FIRST_USER_NAME)));

        contacts.add(anotherUserContact);
        assertEquals(contacts, Set.copyOf(contactRepository.findAll()));
    }

    @Test
    void createThrowsExceptionsOnWrongMethod() {
        Executable wrongMethodExecutable = () -> mainUserWebClient.post().bodyValue(WRONG_REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, wrongMethodExecutable);
    }

    @Test
    void createThrowsExceptionsOnAlreadyExisting() {
        mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Executable alreadyExistsExecutable = () -> mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.Forbidden.class, alreadyExistsExecutable);
    }

    @Test
    void createThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void updateNormalBehavior() {
        Contact initial = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUser = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact updated = mainUserWebClient.patch().bodyValue(UPDATE_REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact inDb = contactRepository.findById(new ContactCompositeKey(FIRST_USER_NAME, Method.SMS, REQUEST.contactId())).orElseThrow();
        Contact anotherUserInDb = contactRepository.findById(new ContactCompositeKey(ANOTHER_USER_NAME, Method.SMS, REQUEST.contactId())).orElseThrow();

        assertNotEquals(initial, inDb);
        assertEquals(updated, inDb);
        assertEquals(anotherUser, anotherUserInDb);
    }

    @Test
    void updateThrowsExceptionsOnWrongMethod() {
        Executable wrongMethodExecutable = () -> mainUserWebClient.patch().bodyValue(WRONG_REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, wrongMethodExecutable);
    }

    @Test
    void updateThrowsExceptionsOnNotExisting() {
        Executable notExistExecutable = () -> mainUserWebClient.patch().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class, notExistExecutable);
    }

    @Test
    void updateThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.patch().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void deleteNormalBehavior() {
        Contact initial = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUser = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact deleted = mainUserWebClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserInDb = contactRepository.findById(new ContactCompositeKey("username1", Method.SMS, REQUEST.contactId())).orElseThrow();
        Executable executable = () -> contactRepository.findById(new ContactCompositeKey("username", Method.SMS, REQUEST.contactId())).orElseThrow();

        assertEquals(new Contact(initial.getAccountId(), initial.getMethod(), initial.getContactId()), deleted);
        assertThrows(NoSuchElementException.class, executable);
        assertEquals(anotherUser, anotherUserInDb);
    }

    @Test
    void deleteThrowsExceptionsOnWrongMethod() {
        Executable wrongMethodExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(WRONG_REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, wrongMethodExecutable);
    }

    @Test
    void deleteThrowsExceptionsOnNotExisting() {
        Executable notExistExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        assertThrows(WebClientResponseException.NotFound.class, notExistExecutable);
    }

    @Test
    void deleteThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void findAllByUsernameNormalBehavior() {
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block(), Collections.emptyList());

        Contact firstUserContact = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        List<Contact> contacts = new ArrayList<>();
        contacts.add(firstUserContact);
        contacts.add(firstUserAnotherContact);
        assertEquals(toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()), contacts);
        assertFalse(toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()).contains(anotherUserContact));
    }

    @Test
    void findAllByUsernameThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.GET).uri("/getByUN").bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void findAllLikePrimaryKeyNormalBehavior() {
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(REQUEST).retrieve().bodyToMono(List.class).block(), Collections.emptyList());

        List<Contact> contacts = new ArrayList<>();
        Contact firstUserContact = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        ContactKeyRequest resultingOneRequest = new ContactKeyRequest(Method.SMS.name(), "38");
        ContactKeyRequest resultingTwoRequest = new ContactKeyRequest(Method.SMS.name(), "");

        contacts.add(firstUserContact);
        assertEquals(contacts, toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingOneRequest).retrieve().bodyToMono(List.class).block()));

        contacts.add(firstUserAnotherContact);
        assertEquals(contacts, toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block()));

        assertEquals(Collections.singletonList(anotherUserContact), toContactList(anotherUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block()));
    }

    @Test
    void findAllLikePrimaryKeyThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void findAllLikeNotificationNameNormalBehavior() {
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(REQUEST).retrieve().bodyToMono(List.class).block(), Collections.emptyList());

        Contact firstUserContact = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        Contact firstUserAnotherContact = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Contact.class).block();
        List<Contact> mainUserContacts = new ArrayList<>();
        mainUserContacts.add(firstUserContact);
        mainUserContacts.add(firstUserAnotherContact);
        Contact firstUserAnotherNotificationContact = mainUserWebClient.post().bodyValue(ANOTHER_NOTIFICATION_REQUEST).retrieve().bodyToMono(Contact.class).block();
        List<Contact> returnedMainUserListName = toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(NOTIFICATION_NAME_REQUEST).retrieve().bodyToMono(List.class).block());
        List<Contact> returnedMainUserListAll = toContactList(mainUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(NOTIFICATION_NAME_REQUEST_BLANK).retrieve().bodyToMono(List.class).block());

        Contact anotherUserContact = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
        List<Contact> returnedAnotherUserList = toContactList(anotherUserWebClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(NOTIFICATION_NAME_REQUEST).retrieve().bodyToMono(List.class).block());


        assertEquals(Collections.singletonList(anotherUserContact), returnedAnotherUserList);
        assertFalse(returnedMainUserListAll.contains(anotherUserContact));
        assertTrue(returnedMainUserListName.equals(mainUserContacts) && !returnedMainUserListName.contains(firstUserAnotherNotificationContact));
        mainUserContacts.add(firstUserAnotherNotificationContact);
        assertEquals(mainUserContacts, returnedMainUserListAll);
    }

    @Test
    void findAllLikeNotificationNameThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.GET).uri("/getByNN").bodyValue(REQUEST).retrieve().bodyToMono(Contact.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    private List<Contact> toContactList(List<LinkedHashMap<String, String>> list) {
        List<Contact> contacts = new ArrayList<>();
        for (LinkedHashMap<String, String> m : list) {
            Contact contact = new Contact(m.get("username"), Method.valueOf(m.get("method")), m.get("contactId"), m.get("notificationName"));
            contacts.add(contact);
        }
        return contacts;
    }

    record User(String username, String password) {
    }
}
