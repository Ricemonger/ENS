package app.security.user.service;

import app.security.config.Configuration;
import app.security.config.JwtUtil;
import app.security.user.controller.exceptions.InvalidPasswordException;
import app.security.user.controller.exceptions.InvalidUsernameException;
import app.security.user.controller.exceptions.UserAlreadyExistsException;
import app.security.user.controller.exceptions.UserDoesntExistException;
import app.security.user.model.User;
import app.security.user.service.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@DataJpaTest
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder = new Configuration().passwordEncoder();

    @Autowired
    private UserRepository userRepository;

    private JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    private UserService userService;
    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository,passwordEncoder,authenticationManager,jwtUtil);
    }

    @Test
    void registerThrowsExceptionOnInvalidCredentials() {
        Map<String, String> invalidUsername = new HashMap<>();
        invalidUsername.put("user","password");
        invalidUsername.put("user12 ","password");
        invalidUsername.put("uuuuuuuuaaaaaaaa11111111111","password");
        for (Map.Entry<String, String> entry : invalidUsername.entrySet()){
            Executable executable = new Executable() {
                @Override
                public void execute() throws Throwable {
                    userService.register(new User(entry.getKey(),entry.getValue()));
                }
            };
            assertThrows(InvalidUsernameException.class,executable);
        }
        Map<String, String> invalidPassword = new HashMap<>();
        invalidPassword.put("username","pass");
        invalidPassword.put("validUser","password1234567890-=12345678");
        invalidPassword.put("username12","password ");
        for (Map.Entry<String, String> entry : invalidPassword.entrySet()){
            Executable executable = new Executable() {
                @Override
                public void execute() throws Throwable {
                    userService.register(new User(entry.getKey(),entry.getValue()));
                }
            };
            assertThrows(InvalidPasswordException.class,executable);
        }
    }

    @Test
    void registerThrowsExceptionIfUserAlreadyExists() {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.register(user);
                userService.register(user);
            }
        };
        assertThrows(UserAlreadyExistsException.class,executable);
    }
    @Test
    void registerNormalBehavior() {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        userService.register(user);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }
    @Test
    void loginThrowsExceptionOnInvalidCredentials() {
        Map<String, String> invalidUsername = new HashMap<>();
        invalidUsername.put("user","password");
        invalidUsername.put("user12 ","password");
        invalidUsername.put("uuuuuuuuaaaaaaaa11111111111","password");
        for (Map.Entry<String, String> entry : invalidUsername.entrySet()){
            Executable executable = new Executable() {
                @Override
                public void execute() throws Throwable {
                    userService.login(new User(entry.getKey(),entry.getValue()));
                }
            };
            assertThrows(InvalidUsernameException.class,executable);
        }
        Map<String, String> invalidPassword = new HashMap<>();
        invalidPassword.put("username","pass");
        invalidPassword.put("validUser","password1234567890-=12345678");
        invalidPassword.put("username12","password ");
        for (Map.Entry<String, String> entry : invalidPassword.entrySet()){
            Executable executable = new Executable() {
                @Override
                public void execute() throws Throwable {
                    userService.login(new User(entry.getKey(),entry.getValue()));
                }
            };
            assertThrows(InvalidPasswordException.class,executable);
        }
    }
    @Test
    void loginThrowsExceptionIfUserDoesntExist() {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.login(user);
            }
        };
        assertThrows(UserDoesntExistException.class,executable);
    }
    @Test
    void loginNormalBehavior() {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        userRepository.save(new User(username,passwordEncoder.encode(password)));
        userService.login(user);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }
}