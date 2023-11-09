package app.security.ens_users.db;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.AnyUser;
import app.security.any_users.db.AnyUserRepositoryService;
import app.security.ens_users.EnsUser;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EnsUserRepositoryService {

    private final EnsUserRepository ensUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    private final AnyUserRepositoryService anyUserRepositoryService;

    public String register(EnsUser ensUser) {
        validateUsernamePassword(ensUser);
        try {
            getByUsernameOrThrow(ensUser.getUsername());
        } catch (UserDoesntExistException e) {
            AnyUser newUserEntry = anyUserRepositoryService.create();

            ensUser.setAccountId(newUserEntry.getAccountId());
            String notEncodedPassword = ensUser.getPassword();
            ensUser.setPassword(passwordEncoder.encode(notEncodedPassword));
            ensUserRepository.save(ensUser);

            ensUser.setPassword(notEncodedPassword);
            log.trace("register method was executed with params:{}", ensUser);
            return login(ensUser);
        }
        throw new UserAlreadyExistsException();
    }

    public String login(EnsUser ensUser) {
        validateUsernamePassword(ensUser);
        String username = ensUser.getUsername();
        String password = ensUser.getPassword();
        EnsUser inDb = getByUsernameOrThrow(username);

        String accountId = inDb.getAccountId();
        throwIfUserDoesntExists(accountId);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String result = abstractUserJwtUtil.generateToken(accountId);
        log.trace("login method was executed with params: accountId-{}, username-{}, password-{} and result: {}",
                accountId,
                username,
                passwordEncoder.encode(password), result);
        return result;
    }

    public boolean canLogin(String username, String password) {
        try {
            validateUsername(username);
            validatePassword(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public EnsUser getByUsername(String username) {
        return getByUsernameOrThrow(username);
    }

    public EnsUser getByAccountId(String accountId) {
        return getByAccountIdOrThrow(accountId);
    }

    public boolean doesUserExist(String accountId) {
        return ensUserRepository.existsByAccountId(accountId);
    }

    private void throwIfUserDoesntExists(String accountId) {
        if (!anyUserRepositoryService.doesUserExist(accountId)) {
            throw new UserDoesntExistException();
        }
    }

    private EnsUser getByUsernameOrThrow(String username) throws UserDoesntExistException {
        try {
            return ensUserRepository.findById(username).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new UserDoesntExistException(e);
        }
    }

    private EnsUser getByAccountIdOrThrow(String accountId) {
        try {
            return ensUserRepository.findByAccountId(accountId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new UserDoesntExistException(e);
        }
    }

    private void validateUsernamePassword(EnsUser ensUser) {
        validateUsername(ensUser.getUsername());
        validatePassword(ensUser.getPassword());
    }

    private void validateUsername(String username) {
        String regex = ".*\\W+.*";
        if (username.length() < 6 || username.length() > 24 || username.matches(regex))
            throw new InvalidUsernameException();
    }

    private void validatePassword(String password) {
        String regex = ".*[\\{\\}\\[\\]\\(\\):;'\".,<>/|\\ ]+.*";
        if (password.length() < 6 || password.length() > 16 || password.matches(regex))
            throw new InvalidPasswordException();
    }
}
