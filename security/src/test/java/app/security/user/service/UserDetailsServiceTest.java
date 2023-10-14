package app.security.user.service;

import app.security.user.model.User;
import app.security.user.model.UserDetails;
import app.security.user.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserDetailsServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "username";
        String password = "password";
        User user = new User(username, password);
        UserDetails userDetails = new UserDetails(user);
        userRepository.save(user);
        assertEquals(userDetails, userDetailsService.loadUserByUsername(username));
    }
}