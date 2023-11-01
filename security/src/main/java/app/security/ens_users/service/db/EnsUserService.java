package app.security.ens_users.service.db;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.JwtUtil;
import app.security.any_users.AnyUser;
import app.security.any_users.db.AnyUserService;
import app.security.ens_users.EnsUser;
import app.security.ens_users.EnsUserDetails;
import app.security.ens_users.exceptions.InvalidIdsException;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class EnsUserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EnsUserRepository ensUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final AnyUserService anyUserService;

    public String register(EnsUser ensUser) {
        validateUsernamePassword(ensUser);
        try {
            getByUsernameOrThrow(ensUser.getUsername());
        } catch (UserDoesntExistException e) {
            AnyUser newUserEntry = anyUserService.create();

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
        doesAccountExist(accountId);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String result = jwtUtil.generateToken(new EnsUserDetails(new EnsUser(accountId, username, password)));
        log.trace("login method was executed with params: accountId-{}, username-{}, password-{} and result: {}",
                accountId,
                username,
                passwordEncoder.encode(password), result);
        return result;
    }

    public EnsUser getByUsername(String username) {
        return getByUsernameOrThrow(username);
    }

    public EnsUser getByAccountId(String accountId) {
        return getByAccountIdOrThrow(accountId);
    }

    private void doesAccountExist(String accountId) {
        if (!anyUserService.doesUserExist(accountId)) {
            throw new InvalidIdsException();
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
