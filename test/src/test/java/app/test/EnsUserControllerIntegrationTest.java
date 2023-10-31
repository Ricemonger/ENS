package app.test;

import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.db.EnsUserRepository;
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
public class EnsUserControllerIntegrationTest {

    @Autowired
    private EnsUserRepository ensUserRepository;

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/api/users").build();

    private static final EnsUser ENS_USER = new EnsUser("username", "password");
    private static final EnsUser ANOTHER_ENS_USER = new EnsUser("username1", "password");

    @BeforeEach
    void beforeEach() {
        ensUserRepository.deleteAll();
    }

    @Test
    void registerNormalBehavior() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(ENS_USER).retrieve().bodyToMono(String.class).block();
        webClient.post().uri("/register").bodyValue(ANOTHER_ENS_USER).retrieve().bodyToMono(String.class).block();

        assertDoesNotThrow(executable);
        assertEquals(ensUserRepository.findById(ENS_USER.getUsername()).orElseThrow().getUsername(), ENS_USER.getUsername());
    }

    @Test
    void registerThrowsExceptionOnAlreadyExists() {
        ensUserRepository.save(new EnsUser("username", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/register").bodyValue(ENS_USER).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void registerThrowsExceptionOnInvalidUsername() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(new EnsUser("us", "password")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void registerThrowsExceptionOnInvalidPassword() {
        Executable executable = () -> webClient.post().uri("/register").bodyValue(new EnsUser("username", "pass")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginNormalBehavior() {
        ensUserRepository.save(new EnsUser("username", new BCryptPasswordEncoder().encode("password")));
        webClient.post().uri("/register").bodyValue(ANOTHER_ENS_USER).retrieve().bodyToMono(String.class).block();
        Executable executable = () -> webClient.post().uri("/login").bodyValue(ENS_USER).retrieve().bodyToMono(String.class).block();

        assertDoesNotThrow(executable);
    }

    @Test
    void loginThrowsExceptionOnDoesntExists() {
        webClient.post().uri("/register").bodyValue(ANOTHER_ENS_USER).retrieve().bodyToMono(String.class).block();
        Executable executable = () -> webClient.post().uri("/login").bodyValue(ENS_USER).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginThrowsExceptionOnBadCredentials() {
        webClient.post().uri("/register").bodyValue(new EnsUser("username1", "password1")).retrieve().bodyToMono(String.class).block();
        ensUserRepository.save(new EnsUser("username", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new EnsUser("username", "password1")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.Unauthorized.class, executable);
    }

    @Test
    void loginThrowsExceptionOnInvalidUsername() {
        ensUserRepository.save(new EnsUser("us", new BCryptPasswordEncoder().encode("password")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new EnsUser("us", "password")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }

    @Test
    void loginThrowsExceptionOnInvalidPassword() {
        ensUserRepository.save(new EnsUser("username", new BCryptPasswordEncoder().encode("pass")));
        Executable executable = () -> webClient.post().uri("/login").bodyValue(new EnsUser("username", "pass")).retrieve().bodyToMono(String.class).block();
        assertThrows(WebClientResponseException.BadRequest.class, executable);
    }
}
