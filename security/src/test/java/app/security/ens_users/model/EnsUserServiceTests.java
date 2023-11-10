package app.security.ens_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.model.db.AnyUserRepository;
import app.security.any_users.model.db.AnyUserRepositoryService;
import app.security.ens_users.EnsUser;
import app.security.ens_users.model.db.EnsUserRepository;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class EnsUserServiceTests {

    private static final EnsUser USER = new EnsUser("username", "password");

    @Autowired
    private EnsUserRepository ensUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AbstractUserJwtUtil abstractUserJwtUtil;

    @Autowired
    private AnyUserRepository anyUserRepository;

    @Autowired
    private AnyUserRepositoryService anyUserRepositoryService;

    @Autowired
    private EnsUserRepositoryService ensUserRepositoryService;

    @BeforeEach
    public void setUp() {
        ensUserRepository.deleteAll();
        anyUserRepository.deleteAll();
    }

    @Test
    public void registerShouldAddUserToDbAndReturnJwt() {

    }
}
