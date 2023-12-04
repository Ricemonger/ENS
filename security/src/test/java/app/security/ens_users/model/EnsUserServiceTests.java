package app.security.ens_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.EnsUser;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.utils.services.security.exceptions.UserAlreadyExistsException;
import app.utils.services.security.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EnsUserServiceTests {

    private static final EnsUser USER = new EnsUser("username", "password");

    private static final EnsUser UPDATED_PASSWORD = new EnsUser("username", "passwordpasswrod");

    private static final EnsUser ANOTHER_USER = new EnsUser("emanresu", "password");

    private static final EnsUser INVALID_USERNAME = new EnsUser("123+=*", "password");

    private static final EnsUser INVALID_PASSWORD = new EnsUser("username", "pass word");

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SpyBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AbstractUserJwtUtil abstractUserJwtUtil;

    @Autowired
    private EnsUserRepositoryService ensUserRepositoryService;

    @Autowired
    private EnsUserService ensUserService;

    @BeforeEach
    public void setUp() {
        ensUserRepositoryService.deleteAll();
    }

    @Test
    public void registerShouldSaveUserInDbWithEncodedPasswordAndAuthenticateAndReturnRightToken() {
        String token = ensUserService.register(USER);

        EnsUser inDb = ensUserRepositoryService.findByIdOrThrow(USER.getUsername());

        assertEquals(USER.getUsername(), inDb.getUsername());
        assertTrue(passwordEncoder.matches(USER.getPassword(), inDb.getPassword()));

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(USER.getUsername(),
                USER.getPassword()));

        assertTrue(abstractUserJwtUtil.isTokenValidAndContainsAccountId(token, inDb.getAccountId()));
    }

    @Test
    public void registerShouldThrowIfInvalidUsername() {
        Executable executable = () -> ensUserService.register(INVALID_USERNAME);

        assertThrows(InvalidUsernameException.class, executable);
    }

    @Test
    public void registerShouldThrowIfInvalidPassword() {
        Executable executable = () -> ensUserService.register(INVALID_PASSWORD);

        assertThrows(InvalidPasswordException.class, executable);
    }

    @Test
    public void registerShouldThrowIfUserAlreadyExists() {
        ensUserRepositoryService.save(USER);

        Executable executable = () -> ensUserService.register(UPDATED_PASSWORD);

        assertThrows(UserAlreadyExistsException.class, executable);
    }

    @Test
    public void loginShouldAuthenticateAndReturnRightTokenIfAuthorized() {
        ensUserRepositoryService.save(new EnsUser(USER.getUsername(), passwordEncoder.encode(USER.getPassword())));

        String token = ensUserService.login(USER);

        EnsUser inDb = ensUserRepositoryService.findByIdOrThrow(USER.getUsername());

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(USER.getUsername(),
                USER.getPassword()));

        assertTrue(abstractUserJwtUtil.isTokenValidAndContainsAccountId(token, inDb.getAccountId()));
    }

    @Test
    public void loginShouldThrowIfUserDoesntExist() {
        ensUserRepositoryService.save(new EnsUser(ANOTHER_USER.getUsername(), passwordEncoder.encode(ANOTHER_USER.getPassword())));

        Executable executable = () -> ensUserService.login(USER);

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void loginShouldThrowIfBadCredentials() {
        ensUserRepositoryService.save(new EnsUser(UPDATED_PASSWORD.getUsername(), passwordEncoder.encode(UPDATED_PASSWORD.getPassword())));

        Executable executable = () -> ensUserService.login(USER);

        assertThrows(BadCredentialsException.class, executable);
    }

    @Test
    public void loginShouldThrowIfInvalidUsername() {
        Executable executable = () -> ensUserService.login(INVALID_USERNAME);

        assertThrows(InvalidUsernameException.class, executable);
    }

    @Test
    public void loginShouldThrowIfInvalidPassword() {
        Executable executable = () -> ensUserService.login(INVALID_PASSWORD);

        assertThrows(InvalidPasswordException.class, executable);
    }

    @Test
    public void canLoginShouldReturnTrueIfUserExistsAndValid() {
        ensUserRepositoryService.save(new EnsUser(USER.getUsername(), passwordEncoder.encode(USER.getPassword())));

        assertTrue(ensUserService.canLogin(USER.getUsername(), USER.getPassword()));
    }

    @Test
    public void canLoginShouldReturnFalseIfUserDoesntExist() {
        assertFalse(ensUserService.canLogin(USER.getUsername(), USER.getPassword()));
    }

    @Test
    public void canLoginShouldReturnFalseIfBadCredentials() {
        ensUserRepositoryService.save(UPDATED_PASSWORD);

        assertFalse(ensUserService.canLogin(USER.getUsername(), USER.getPassword()));
    }

    @Test
    public void canLoginShouldReturnFalseIfInvalidUsername() {
        ensUserRepositoryService.save(INVALID_USERNAME);

        assertFalse(ensUserService.canLogin(INVALID_USERNAME.getUsername(), INVALID_USERNAME.getPassword()));
    }

    @Test
    public void canLoginShouldReturnFalseIfInvalidPassword() {
        ensUserRepositoryService.save(INVALID_PASSWORD);

        assertFalse(ensUserService.canLogin(INVALID_PASSWORD.getUsername(), INVALID_PASSWORD.getPassword()));
    }

    @Test
    public void getByUsernameOrThrowShouldFindUserInDb() {
        EnsUser saved = ensUserRepositoryService.save(USER);

        EnsUser trueResult = ensUserService.getByUsernameOrThrow(USER.getUsername());

        assertEquals(saved, trueResult);
    }

    @Test
    public void getByUsernameOrThrowShouldThrowIfDoesntExist() {
        ensUserRepositoryService.save(ANOTHER_USER);

        Executable executable = () -> ensUserService.getByUsernameOrThrow(USER.getUsername());

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    void getByAccountIdOrThrowShouldFindUserInDb() {
        EnsUser saved = ensUserRepositoryService.save(USER);

        EnsUser trueResult = ensUserService.getByAccountIdOrThrow(saved.getAccountId());

        assertEquals(saved, trueResult);
    }

    @Test
    void getByAccountIdOrThrowShouldThrowIfDoesntExist() {
        ensUserRepositoryService.save(ANOTHER_USER);

        Executable executable = () -> ensUserService.getByAccountIdOrThrow(USER.getAccountId());

        assertThrows(UserDoesntExistException.class, executable);
    }

    @Test
    public void doesUserExistsShouldReturnTrueIfExists() {
        String accountId = ensUserRepositoryService.save(USER).getAccountId();

        assertTrue(ensUserService.doesUserExist(accountId));
    }

    @Test
    public void doesUserExistsShouldReturnFalseIfDoesntExist() {
        assertFalse(ensUserService.doesUserExist("hjhjh"));
    }
}
