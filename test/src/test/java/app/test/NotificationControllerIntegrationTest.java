package app.test;

import app.contact.service.repository.ContactRepository;
import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.model.Notification;
import app.notification.model.NotificationCompositeKey;
import app.notification.service.repository.NotificationRepository;
import app.security.user.service.repository.UserRepository;
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
public class NotificationControllerIntegrationTest {

    private WebClient mainUserWebClient;

    private WebClient anotherUserWebClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final NotificationCreUpdRequest REQUEST = new NotificationCreUpdRequest("name", "text");
    private static final NotificationCreUpdRequest UPDATE_REQUEST = new NotificationCreUpdRequest("name", "new text");
    private static final NotificationCreUpdRequest ANOTHER_REQUEST = new NotificationCreUpdRequest("another name", "text");

    private static final String FIRST_USER_NAME = "username";
    private static final String ANOTHER_USER_NAME = "username1";

    public InvalidJwts invalidJwts = new InvalidJwts("http://localhost:8080/api/notifications");

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
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
                .baseUrl("http://localhost:8080/api/notifications")
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
                .baseUrl("http://localhost:8080/api/notifications")
                .defaultHeader("Authorization", "Bearer " + token1)
                .build();
    }

    @Test
    void createNormalBehavior() {
        Notification firstUserNotification = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification firstUserAnotherNotification = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUserNotification = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Set<Notification> notifications = new HashSet<>();
        notifications.add(firstUserNotification);
        notifications.add(firstUserAnotherNotification);

        assertEquals(firstUserNotification, notificationRepository.findById(new NotificationCompositeKey(FIRST_USER_NAME, REQUEST.name())).orElseThrow());

        assertEquals(notifications, Set.copyOf(notificationRepository.findAllByUsername(FIRST_USER_NAME)));

        notifications.add(anotherUserNotification);
        assertEquals(notifications, Set.copyOf(notificationRepository.findAll()));
    }

    @Test
    void createThrowsExceptionsOnAlreadyExisting() {
        mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Executable alreadyExistsExecutable = () -> mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        assertThrows(WebClientResponseException.Forbidden.class, alreadyExistsExecutable);
    }

    @Test
    void createThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void updateNormalBehavior() {
        Notification initial = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUser = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification updated = mainUserWebClient.patch().bodyValue(UPDATE_REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification inDb = notificationRepository.findById(new NotificationCompositeKey(FIRST_USER_NAME, REQUEST.name())).orElseThrow();
        Notification anotherUserInDb = notificationRepository.findById(new NotificationCompositeKey(ANOTHER_USER_NAME, REQUEST.name())).orElseThrow();

        assertNotEquals(initial, inDb);
        assertEquals(updated, inDb);
        assertEquals(anotherUser, anotherUserInDb);
    }

    @Test
    void updateThrowsExceptionsOnNotExisting() {
        Executable notExistExecutable = () -> mainUserWebClient.patch().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        assertThrows(WebClientResponseException.NotFound.class, notExistExecutable);
    }

    @Test
    void updateThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.patch().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void deleteNormalBehavior() {
        Notification initial = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUser = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification deleted = mainUserWebClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUserInDb = notificationRepository.findById(new NotificationCompositeKey(ANOTHER_USER_NAME, REQUEST.name())).orElseThrow();
        Executable executable = () -> notificationRepository.findById(new NotificationCompositeKey(FIRST_USER_NAME, REQUEST.name())).orElseThrow();

        assertEquals(initial, deleted);
        assertThrows(NoSuchElementException.class, executable);
        assertEquals(anotherUser, anotherUserInDb);
    }

    @Test
    void deleteThrowsExceptionsOnNotExisting() {
        Executable notExistExecutable = () -> mainUserWebClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        assertThrows(WebClientResponseException.NotFound.class, notExistExecutable);
    }

    @Test
    void deleteThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.DELETE).bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void findAllByUsernameNormalBehavior() {
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block(), Collections.emptyList());

        Notification firstUserNotification = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification firstUserAnotherNotification = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUserNotification = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        List<Notification> notifications = new ArrayList<>();
        notifications.add(firstUserNotification);
        notifications.add(firstUserAnotherNotification);
        assertEquals(toNotificationList(mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()), notifications);
        assertFalse((mainUserWebClient.method(HttpMethod.GET).uri("/getByUN").retrieve().bodyToMono(List.class).block()).contains(anotherUserNotification));
    }

    @Test
    void findAllByUsernameThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.GET).uri("/getByUN").bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    @Test
    void findAllLikePrimaryKeyNormalBehavior() {
        assertEquals(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(REQUEST).retrieve().bodyToMono(List.class).block(), Collections.emptyList());

        Set<Notification> notifications = new HashSet<>();
        Notification firstUserNotification = mainUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification firstUserAnotherNotification = mainUserWebClient.post().bodyValue(ANOTHER_REQUEST).retrieve().bodyToMono(Notification.class).block();
        Notification anotherUserNotification = anotherUserWebClient.post().bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
        NotificationNameRequest resultingOneRequest = new NotificationNameRequest(REQUEST.name());
        NotificationNameRequest resultingTwoRequest = new NotificationNameRequest("");

        notifications.add(firstUserNotification);
        assertEquals(notifications, Set.copyOf(toNotificationList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingOneRequest).retrieve().bodyToMono(List.class).block())));

        notifications.add(firstUserAnotherNotification);
        assertEquals(notifications, Set.copyOf(toNotificationList(mainUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block())));

        assertEquals(Collections.singletonList(anotherUserNotification), (anotherUserWebClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(resultingTwoRequest).retrieve().bodyToMono(List.class).block()));
    }

    @Test
    void findAllLikePrimaryKeyThrowsExceptionsOnInvalidJwt() {
        for (WebClient webClient : invalidJwts.getInvalidWebClientList()) {
            Executable executable = () -> webClient.method(HttpMethod.GET).uri("/getByPK").bodyValue(REQUEST).retrieve().bodyToMono(Notification.class).block();
            assertThrows(JwtException.class, executable);
        }
    }

    private List<Notification> toNotificationList(List<LinkedHashMap<String, String>> list) {
        List<Notification> notifications = new ArrayList<>();
        for (LinkedHashMap<String, String> m : list) {
            Notification notification = new Notification(m.get("username"), m.get("name"), m.get("text"));
            notifications.add(notification);
        }
        return notifications;
    }

    record User(String username, String password) {
    }
}
