package app.security.ens_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.EnsUser;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.utils.feign_clients.security.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnsUserService {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    private final EnsUserRepositoryService ensUserRepositoryService;

    public String register(EnsUser ensUser) {
        validateUsernamePassword(ensUser);
        if (ensUserRepositoryService.existsByUsername(ensUser.getUsername())) {
            String notEncodedPassword = ensUser.getPassword();
            ensUser.setPassword(passwordEncoder.encode(notEncodedPassword));
            ensUserRepositoryService.save(ensUser);

            ensUser.setPassword(notEncodedPassword);
            log.trace("register method was executed with params:{}", ensUser);
            return login(ensUser);
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public String login(EnsUser ensUser) {
        validateUsernamePassword(ensUser);
        String username = ensUser.getUsername();
        String password = ensUser.getPassword();

        EnsUser inDb = getByUsernameOrThrow(username);

        String accountId = inDb.getAccountId();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String result = abstractUserJwtUtil.generateToken(accountId);
        log.trace("login method was executed with params: accountId-{}, username-{}, password-{} and result: {}",
                accountId,
                username,
                passwordEncoder.encode(password), result);
        return result;
    }

    public boolean canLogin(String username, String password) {
        boolean result = true;
        try {
            validateUsername(username);
            validatePassword(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            result = false;
        }
        log.trace("canLogin was executed for username-{}, password-{} with result-{}", username, password, result);
        return result;
    }

    public EnsUser getByUsernameOrThrow(String username) {
        log.trace("getByUsernameOrThrow was called for username-{}", username);
        return ensUserRepositoryService.findByIdOrThrow(username);
    }

    public boolean doesUserExist(String accountId) {
        boolean result = ensUserRepositoryService.existsByAccountId(accountId);
        log.trace("doesUserExists was called for accountId-{} with result-{}", accountId, result);
        return result;
    }

    private void validateUsernamePassword(EnsUser ensUser) {
        validateUsername(ensUser.getUsername());
        validatePassword(ensUser.getPassword());
    }

    private void validateUsername(String username) {
        String regex = ".*\\W+.*";
        if (username.length() < 6 || username.length() > 24 || username.matches(regex)) {
            log.trace("Username {} is not valid!", username);
            throw new InvalidUsernameException();
        }
    }

    private void validatePassword(String password) {
        String regex = ".*[\\{\\}\\[\\]\\(\\):;'\".,<>/|\\ ]+.*";
        if (password.length() < 6 || password.length() > 16 || password.matches(regex)) {
            log.trace("Password {} is not valid!", password);
            throw new InvalidPasswordException();
        }
    }
}
