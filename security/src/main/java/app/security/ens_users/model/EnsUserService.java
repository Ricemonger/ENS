package app.security.ens_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.EnsUser;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.security.ens_users.model.db.EnsUserRepositoryService;
import app.utils.services.security.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
        if (!ensUserRepositoryService.existsByUsername(ensUser.getUsername())) {
            String notEncodedPassword = ensUser.getPassword();
            ensUser.setPassword(passwordEncoder.encode(notEncodedPassword));
            ensUserRepositoryService.save(ensUser);

            ensUser.setPassword(notEncodedPassword);
            return login(ensUser);
        } else {
            log.info("register called for ensUser-{}, user already exists", ensUser);
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

        return abstractUserJwtUtil.generateToken(accountId);
    }

    public boolean canLogin(String username, String password) {
        boolean result = true;
        try {
            validateUsername(username);
            validatePassword(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (InvalidUsernameException | InvalidPasswordException | AuthenticationException e) {
            result = false;
        }
        return result;
    }

    public EnsUser getByUsernameOrThrow(String username) {
        return ensUserRepositoryService.findByIdOrThrow(username);
    }

    public EnsUser getByAccountIdOrThrow(String accountId) {
        return ensUserRepositoryService.findByAccountIdOrThrow(accountId);
    }

    public boolean doesUserExist(String accountId) {
        return ensUserRepositoryService.existsByAccountId(accountId);
    }

    private void validateUsernamePassword(EnsUser ensUser) {
        validateUsername(ensUser.getUsername());
        validatePassword(ensUser.getPassword());
    }

    private void validateUsername(String username) {
        String regex = ".*\\W+.*";
        if (username.length() < 6 || username.length() > 24 || username.matches(regex)) {
            log.info("validateUsername executed for username-{}, invalid username", username);
            throw new InvalidUsernameException();
        }
    }

    private void validatePassword(String password) {
        String regex = ".*[\\{\\}\\[\\]\\(\\):;'\".,<>/|\\ ]+.*";
        if (password.length() < 6 || password.length() > 16 || password.matches(regex)) {
            log.info("validateUsername executed for password-{}, invalid password", password);
            throw new InvalidPasswordException();
        }
    }
}
