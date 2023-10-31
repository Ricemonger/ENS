package app.security.user.service;

import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.EnsUserDetails;
import app.security.user.service.ens_user.db.EnsUserDetailsService;
import app.security.user.service.ens_user.db.EnsUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class EnsEnsUserDetailsServiceTest {

    @Autowired
    private EnsUserRepository ensUserRepository;

    private EnsUserDetailsService ensUserDetailsService;

    @BeforeEach
    void setUp() {
        ensUserDetailsService = new EnsUserDetailsService(ensUserRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "username";
        String password = "password";
        EnsUser ensUser = new EnsUser(username, password);
        EnsUserDetails ensUserDetails = new EnsUserDetails(ensUser);
        ensUserRepository.save(ensUser);
        assertEquals(ensUserDetails, ensUserDetailsService.loadUserByUsername(username));
    }
}