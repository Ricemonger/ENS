package app.security.any_users.db;

import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.any_users.AnyUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnyUserService {

    private final AnyUserRepository anyUserRepository;

    public AnyUser create() {
        AnyUser result = anyUserRepository.save(new AnyUser());
        log.trace("create method called with result:{}", result);
        return result;
    }

    public AnyUser delete(String accountId) {
        try {
            AnyUser deleted = anyUserRepository.findById(accountId).orElseThrow();
            anyUserRepository.deleteById(accountId);
            log.trace("user {} was deleted", deleted);
            return deleted;
        } catch (NoSuchElementException e) {
            log.error("delete method was called for accountID {}, but user doesn't exist", accountId);
            throw new UserDoesntExistException();
        }
    }

    public boolean doesUserExist(String accountId) {
        boolean result = anyUserRepository.existsById(accountId);
        log.trace("doesUserExist method was called with accountID-{} and result-{}", accountId, result);
        return result;
    }
}