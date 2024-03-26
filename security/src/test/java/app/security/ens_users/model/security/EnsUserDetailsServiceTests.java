package app.security.ens_users.model.security;

import app.security.ens_users.EnsUser;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.utils.services.security.exceptions.SecurityUserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EnsUserDetailsServiceTests {

    private final static EnsUser USER = new EnsUser("username", "password");

    private final static EnsUser ANOTHER_USER = new EnsUser("emanresu", "drowssap");

    @Autowired
    private EnsUserRepositoryService repositoryService;

    @Autowired
    private EnsUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        repositoryService.deleteAll();
    }

    @Test
    public void loadByUsernameShouldFindInDbIfExists() {
        String accountId = repositoryService.save(USER).getAccountId();

        EnsUserDetails userDetails = userDetailsService.loadUserByUsername(USER.getUsername());

        EnsUser expected = new EnsUser(accountId, USER.getUsername(), USER.getPassword());

        EnsUser fromDetails = new EnsUser();
        fromDetails.setUsername(userDetails.getUsername());
        fromDetails.setPassword(userDetails.getPassword());
        fromDetails.setAccountId(userDetails.getAccountId());

        assertEquals(expected, fromDetails);
    }

    @Test
    public void loadByUsernameShouldThrowIfDoesntExist() {
        repositoryService.save(ANOTHER_USER);

        Executable executable = () -> userDetailsService.loadUserByUsername(USER.getUsername());

        assertThrows(SecurityUserDoesntExistException.class, executable);
    }

    @Test
    public void loadByAccountIdShouldFindByAccountIdIfExists() {
        String accountId = repositoryService.save(USER).getAccountId();

        EnsUserDetails userDetails = userDetailsService.loadUserByAccountId(accountId);

        EnsUser expected = new EnsUser(accountId, USER.getUsername(), USER.getPassword());

        EnsUser fromDetails = new EnsUser();
        fromDetails.setUsername(userDetails.getUsername());
        fromDetails.setPassword(userDetails.getPassword());
        fromDetails.setAccountId(userDetails.getAccountId());

        assertEquals(expected, fromDetails);
    }

    @Test
    public void loadByAccountIdShouldThrowIfDoesntExist() {
        repositoryService.save(USER);

        Executable executable = () -> userDetailsService.loadUserByAccountId("uyuyt657");

        assertThrows(SecurityUserDoesntExistException.class, executable);
    }
}
