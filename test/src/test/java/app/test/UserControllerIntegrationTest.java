package app.test;

import app.security.user.model.User;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
public class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/api/users").build();

    private static final User USER = new User("username", "password");
    private static final User ANOTHER_USER = new User("username1", "password");

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    void registerNormalBehavior() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(USER).retrieve().bodyToMono(String.class).block();
        webClient.post().uri("/register").bodyValue(ANOTHER_USER).retrieve().bodyToMono(String.class).block();

        assertDoesNotThrow(executable);
        assertEquals(userRepository.findById(USER.getUsername()).orElseThrow().getUsername(), USER.getUsername());
    }

    @Test
    void registerThrowsExceptionOnAlreadyExists() {
        userRepository.save(new User("username", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/register").bodyValue(USER).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void registerThrowsExceptionOnInvalidUsername() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(new User("us", "password")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void registerThrowsExceptionOnInvalidPassword() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(new User("username", "pass")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginNormalBehavior() {
        userRepository.save(new User("username", new BCryptPasswordEncoder().encode("password")));
        webClient.post().uri("/register").bodyValue(ANOTHER_USER).retrieve().bodyToMono(String.class).block();
        Executable executable = () -> webClient.post().uri("/login").bodyValue(USER).retrieve().bodyToMono(String.class).block();

        assertDoesNotThrow(executable);
    }

    @Test
    void loginThrowsExceptionOnDoesntExists() {
        webClient.post().uri("/register").bodyValue(ANOTHER_USER).retrieve().bodyToMono(String.class).block();
        Executable executable = () -> webClient.post().uri("/login").bodyValue(USER).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginThrowsExceptionOnBadCredentials() {
        webClient.post().uri("/register").bodyValue(new User("username1", "password1")).retrieve().bodyToMono(String.class).block();
        userRepository.save(new User("username", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new User("username", "password1")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.Unauthorized.class, executable);
    }

    @Test
    void loginThrowsExceptionOnInvalidUsername() {
        userRepository.save(new User("us", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new User("us", "password")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginThrowsExceptionOnInvalidPassword() {
        userRepository.save(new User("username", new BCryptPasswordEncoder().encode("pass")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new User("username", "pass")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }
}
