package app.security.any_users.db;

import app.security.any_users.AnyUser;
import app.security.ens_users.exceptions.UserDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AnyUserService {

    private final AnyUserRepository anyUserRepository;

    public AnyUser create() {
        return anyUserRepository.save(new AnyUser());
    }

    public AnyUser delete(String accountId) {
        try {
            AnyUser deleted = anyUserRepository.findById(accountId).orElseThrow();
            anyUserRepository.deleteById(accountId);
            return deleted;
        } catch (NoSuchElementException e) {
            throw new UserDoesntExistException();
        }
    }

    public boolean doesUserExist(String accountId) {
        return anyUserRepository.existsById(accountId);
    }
}