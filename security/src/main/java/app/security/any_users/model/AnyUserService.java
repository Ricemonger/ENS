package app.security.any_users.model;

import app.security.any_users.AnyUser;
import app.security.any_users.model.db.AnyUserRepositoryService;
import app.utils.services.security.exceptions.SecurityUserDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AnyUserService {

    private final AnyUserRepositoryService anyUserRepositoryService;


    public AnyUser create() {
        return anyUserRepositoryService.save(new AnyUser());
    }

    public AnyUser delete(String accountId) {
        try {
            AnyUser deleted = anyUserRepositoryService.findByIdOrThrow(accountId);
            anyUserRepositoryService.deleteById(accountId);
            return deleted;
        } catch (NoSuchElementException e) {
            log.info("delete method was called for accountID-{}, user doesn't exist", accountId);
            throw new SecurityUserDoesntExistException();
        }
    }

    public boolean doesUserExist(String accountId) {
        return anyUserRepositoryService.existsById(accountId);
    }
}
