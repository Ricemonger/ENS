package app.test;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.model.Contact;
import app.contact.model.ContactCompositeKey;
import app.contact.model.Method;
import app.contact.service.repository.ContactRepository;
import app.notification.service.repository.NotificationRepository;
import app.security.user.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void create(){
        ContactCreUpdRequest request = new ContactCreUpdRequest("SMS","380","name");
        Contact contact = mainUserWebClient.post().bodyValue(request).retrieve().bodyToMono(Contact.class).block();
        assertEquals(contact,contactRepository.findById(new ContactCompositeKey("username", Method.SMS,"380")).orElseThrow());
    }
    record User(String username, String password){}
}
