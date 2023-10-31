package app.security.user.service.ens_user.db;

import app.security.security.JwtUtil;
import app.security.user.exceptions.InvalidPasswordException;
import app.security.user.exceptions.InvalidUsernameException;
import app.security.user.exceptions.UserAlreadyExistsException;
import app.security.user.exceptions.UserDoesntExistException;
import app.security.user.service.ens_user.EnsUser;
import app.security.user.service.ens_user.EnsUserDetails;
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

    public String register(EnsUser ensUser) {
        validate(ensUser);
        try {
            getByIdOrThrow(ensUser.getUsername());
        } catch (UserDoesntExistException e) {
            String password = ensUser.getPassword();
            ensUser.setPassword(passwordEncoder.encode(password));
            ensUserRepository.save(ensUser);
            ensUser.setPassword(password);
            log.trace("register method was executed with params:{}", ensUser);
            return login(ensUser);
        }
        throw new UserAlreadyExistsException();
    }

    public String login(EnsUser ensUser) {
        validate(ensUser);
        String accountId = ensUser.getAccountId();
        String username = ensUser.getUsername();
        String password = ensUser.getPassword();
        getByIdOrThrow(username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String result = jwtUtil.generateToken(new EnsUserDetails(new EnsUser(accountId, username, password)));
        log.trace("login method was executed with params: username-{}, password-{} and result: {}", username, passwordEncoder.encode(password), result);
        return result;
    }

    private EnsUser getByIdOrThrow(String username) throws UserDoesntExistException {
        try {
            return ensUserRepository.findById(username).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new UserDoesntExistException(e);
        }
    }

    private void validate(EnsUser ensUser) {
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
