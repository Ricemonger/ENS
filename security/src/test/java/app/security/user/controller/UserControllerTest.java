package app.security.user.controller;

import app.security.security.JwtUtil;
import app.security.user.controller.dto.UserLoginRequest;
import app.security.user.controller.dto.UserRegisterRequest;
import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.EnsUserDetails;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final JwtUtil jwtUtil = new JwtUtil("b7221331a051cdc4cafcab5884a0d9723d6ed94eaab70233b000442b1302c9eb");

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EnsUserRepository ensUserRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    private EnsUserService ensUserService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        ensUserService = new EnsUserService(ensUserRepository, passwordEncoder, authenticationManager, jwtUtil);
        userController = new UserController(ensUserService);
    }

    @Test
    void registerNormalBehavior() {
        String username = "username";
        String password = "password";
        UserRegisterRequest request = new UserRegisterRequest(username, password);
        String result = userController.register(request);
        assertTrue(jwtUtil.isTokenValid(result, new EnsUserDetails(new EnsUser(username, password))));
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Test
    void registerThrowsExceptionsOnInvalidCredentials() {
        Map<String, String> map = new HashMap<>();
        map.put("user", "password");
        map.put("username", "pass");
        map.put("user''", "password");
        map.put("username1", "pass''");
        System.out.println(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            UserRegisterRequest request = new UserRegisterRequest(entry.getKey(), entry.getValue());
            Executable executable = () -> {
                System.out.println(request);
                userController.register(request);
            };
            assertThrows(RuntimeException.class, executable);
            verifyNoInteractions(authenticationManager);
        }
    }

    @Test
    void loginNormalBehavior() {
        String username = "username";
        String password = "password";
        ensUserRepository.save(new EnsUser(username, password));
        UserLoginRequest request = new UserLoginRequest(username, password);
        String result = userController.login(request);
        assertTrue(jwtUtil.isTokenValid(result, new EnsUserDetails(new EnsUser(username, password))));
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Test
    void loginThrowsExceptionsOnInvalidCredentials() {
        Map<String, String> map = new HashMap<>();
        map.put("user", "password");
        map.put("username", "pass");
        map.put("user''", "password");
        map.put("username1", "pass''");
        System.out.println(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            UserLoginRequest request = new UserLoginRequest(entry.getKey(), entry.getValue());
            Executable executable = () -> {
                System.out.println(request);
                userController.login(request);
            };
            assertThrows(RuntimeException.class, executable);
            verifyNoInteractions(authenticationManager);
        }
    }
}