package app.security.user.service;

import app.security.security.Configuration;
import app.security.security.JwtUtil;
import app.security.user.exceptions.InvalidPasswordException;
import app.security.user.exceptions.InvalidUsernameException;
import app.security.user.exceptions.UserAlreadyExistsException;
import app.security.user.exceptions.UserDoesntExistException;
import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.db.EnsUserRepository;
import app.security.user.service.ens_user.db.EnsUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@DataJpaTest
class EnsEnsUserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder = new Configuration().passwordEncoder();

    @Autowired
    private EnsUserRepository ensUserRepository;

    private final JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    private EnsUserService ensUserService;

    @BeforeEach
    void setUp() {
        ensUserService = new EnsUserService(ensUserRepository, passwordEncoder, authenticationManager, jwtUtil);
    }

    @Test
    void registerThrowsExceptionOnInvalidCredentials() {
        Map<String, String> invalidUsername = new HashMap<>();
        invalidUsername.put("user", "password");
        invalidUsername.put("user12 ", "password");
        invalidUsername.put("uuuuuuuuaaaaaaaa11111111111", "password");
        for (Map.Entry<String, String> entry : invalidUsername.entrySet()) {
            Executable executable = () -> ensUserService.register(new EnsUser(entry.getKey(), entry.getValue()));
            assertThrows(InvalidUsernameException.class, executable);
        }
        Map<String, String> invalidPassword = new HashMap<>();
        invalidPassword.put("username", "pass");
        invalidPassword.put("validUser", "password1234567890-=12345678");
        invalidPassword.put("username12", "password ");
        for (Map.Entry<String, String> entry : invalidPassword.entrySet()) {
            Executable executable = () -> ensUserService.register(new EnsUser(entry.getKey(), entry.getValue()));
            assertThrows(InvalidPasswordException.class, executable);
        }
    }

    @Test
    void registerThrowsExceptionIfUserAlreadyExists() {
        String username = "username";
        String password = "password";
        EnsUser ensUser = new EnsUser(username, password);
        Executable executable = () -> {
            ensUserService.register(ensUser);
            ensUserService.register(ensUser);
        };
        assertThrows(UserAlreadyExistsException.class, executable);
    }

    @Test
    void registerNormalBehavior() {
        String username = "username";
        String password = "password";
        EnsUser ensUser = new EnsUser(username, password);
        ensUserService.register(ensUser);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Test
    void loginThrowsExceptionOnInvalidCredentials() {
        Map<String, String> invalidUsername = new HashMap<>();
        invalidUsername.put("user", "password");
        invalidUsername.put("user12 ", "password");
        invalidUsername.put("uuuuuuuuaaaaaaaa11111111111", "password");
        for (Map.Entry<String, String> entry : invalidUsername.entrySet()) {
            Executable executable = () -> ensUserService.login(new EnsUser(entry.getKey(), entry.getValue()));
            assertThrows(InvalidUsernameException.class, executable);
        }
        Map<String, String> invalidPassword = new HashMap<>();
        invalidPassword.put("username", "pass");
        invalidPassword.put("validUser", "password1234567890-=12345678");
        invalidPassword.put("username12", "password ");
        for (Map.Entry<String, String> entry : invalidPassword.entrySet()) {
            Executable executable = () -> ensUserService.login(new EnsUser(entry.getKey(), entry.getValue()));
            assertThrows(InvalidPasswordException.class, executable);
        }
    }

    @Test
    void loginThrowsExceptionIfUserDoesntExist() {
        String username = "username";
        String password = "password";
        EnsUser ensUser = new EnsUser(username, password);
        Executable executable = () -> ensUserService.login(ensUser);
        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    void loginNormalBehavior() {
        String username = "username";
        String password = "password";
        EnsUser ensUser = new EnsUser(username, password);
        ensUserRepository.save(new EnsUser(username, passwordEncoder.encode(password)));
        ensUserService.login(ensUser);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}