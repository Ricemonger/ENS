package app.security.ens_users.model.db;

import app.security.ens_users.EnsUser;
import app.utils.feign_clients.security.exceptions.UserDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EnsUserRepositoryService {

    private final EnsUserRepository ensUserRepository;

    public EnsUser save(EnsUser ensUser) {
        EnsUser result = toEnsUser(ensUserRepository.save(toEnsUserEntity(ensUser)));
        log.trace("save was called for ensUser-{}, result-{}", ensUser, result);
        return result;
    }

    public void deleteAll() {
        log.trace("deleteAll was called");
        ensUserRepository.deleteAll();
    }

    public boolean existsByAccountId(String accountId) {
        boolean result = ensUserRepository.existsByAnyUserEntityAccountId(accountId);
        log.trace("save was called for accountId-{}, result-{}", accountId, result);
        return result;
    }

    public boolean existsByUsername(String username) {
        boolean result = ensUserRepository.existsById(username);
        log.trace("save was called for username-{}, result-{}", username, result);
        return result;
    }

    public EnsUser findByIdOrThrow(String username) {
        try {
            log.trace("findByIdOrThrow was called for username-{}", username);
            return toEnsUser(ensUserRepository.findById(username).orElseThrow());
        } catch (NoSuchElementException e) {
            log.error("findByIdOrThrow throws UserDoesntExist for username-{}", username);
            throw new UserDoesntExistException();
        }
    }

    public EnsUser findByAccountIdOrThrow(String accountId) {
        try {
            log.trace("findByAccountIdOrThrow was called for accountId-{}", accountId);
            return toEnsUser(ensUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
        } catch (NoSuchElementException e) {
            log.error("findByAccountIdOrThrow throws UserDoesntExist for accountId-{}", accountId);
            throw new UserDoesntExistException();
        }
    }

    private EnsUser toEnsUser(EnsUserEntity entity) {
        return new EnsUser(entity.getAccountId(), entity.getUsername(), entity.getPassword());
    }

    private EnsUserEntity toEnsUserEntity(EnsUser ensUser) {
        return new EnsUserEntity(ensUser.getAccountId(), ensUser.getUsername(), ensUser.getPassword());
    }
}
