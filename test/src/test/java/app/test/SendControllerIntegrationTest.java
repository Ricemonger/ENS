package app.test;

import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.service.repository.ContactRepository;
import app.notification.model.Notification;
import app.notification.service.repository.NotificationRepository;
import app.security.user.service.repository.UserRepository;
import app.send.controller.dto.SendOneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@EnableJpaRepositories({"app.test"
        , "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.repository"})
@ComponentScan(basePackages = {"app.test"
        , "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.repository"})
@EntityScan({
        "app.contact.model", "app.contact.service.repository"
        , "app.notification.model", "app.notification.service.repository"
        , "app.security.user.model", "app.security.user.service.repository"})
@EnableConfigurationProperties
public class SendControllerIntegrationTest {

    private WebClient mainUserWebClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void beforeEach() {
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
                .bodyValue(new NotificationControllerIntegrationTest.User(username, password))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        mainUserWebClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api/send")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    //Text - test message
    @Test
    void sendOneSms() {
        SendOneRequest request = new SendOneRequest("SMS", "380953766409", "test message");
        mainUserWebClient.post().uri("/one").bodyValue(request).retrieve().toBodilessEntity().block();
        System.out.printf("%s message sent to phone number +%s with message:%s\n", request.method(), request.contactId(), request.notificationText());
    }

    //Text - test message
    @Test
    void sendOneViber() {
        SendOneRequest request = new SendOneRequest("VIBER", "380953766409", "test message");
        mainUserWebClient.post().uri("/one").bodyValue(request).retrieve().toBodilessEntity().block();
        System.out.printf("%s message sent to phone number +%s with message:%s\n", request.method(), request.contactId(), request.notificationText());
    }

    //Text - test message
    @Test
    void sendOneEmail() {
        SendOneRequest request = new SendOneRequest("EMAIL", "leskotr23@gmail.com", "test message");
        mainUserWebClient.post().uri("/one").bodyValue(request).retrieve().toBodilessEntity().block();
        System.out.printf("%s message sent to phone number +%s with message:%s\n", request.method(), request.contactId(), request.notificationText());
    }

    //Text - test message form database
    @Test
    void sendOneEmailFormDB() {
        contactRepository.save(new Contact("username", Method.EMAIL, "leskotr23@gmail.com", "notificationName"));
        notificationRepository.save(new Notification("username", "notificationName", "test message from database"));
        SendOneRequest request = new SendOneRequest("EMAIL", "leskotr23@gmail.com", null);
        mainUserWebClient.post().uri("/one").bodyValue(request).retrieve().toBodilessEntity().block();
        System.out.printf("%s message sent to phone number +%s with message:%s\n", request.method(), request.contactId(), request.notificationText());
    }

    //Text - EMERGENCY NOTIFICATION MESSAGE BY %S!!
    @Test
    void sendOneEmailDefaultMessage() {
        SendOneRequest request = new SendOneRequest("EMAIL", "leskotr23@gmail.com", null);
        mainUserWebClient.post().uri("/one").bodyValue(request).retrieve().toBodilessEntity().block();
        System.out.printf("%s message sent to phone number +%s with message:%s\n", request.method(), request.contactId(), request.notificationText());
    }

    //Text - test message bull
    @Test
    void sendAll() {
        contactRepository.save(new Contact("username", Method.EMAIL, "leskotr23@gmail.com", "notificationName"));
        contactRepository.save(new Contact("username", Method.VIBER, "380953766409", "notificationName"));
        contactRepository.save(new Contact("username", Method.SMS, "380953766409", "notificationName"));
        notificationRepository.save(new Notification("username", "notificationName", "test message bulk"));
        mainUserWebClient.post().uri("/all").retrieve().toBodilessEntity().block();
    }
}
