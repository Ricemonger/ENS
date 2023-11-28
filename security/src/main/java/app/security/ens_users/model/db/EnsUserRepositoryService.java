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
        return toEnsUser(ensUserRepository.save(toEnsUserEntity(ensUser)));
    }

    public void deleteAll() {
        ensUserRepository.deleteAll();
    }

    public boolean existsByAccountId(String accountId) {
        return ensUserRepository.existsByAnyUserEntityAccountId(accountId);
    }

    public boolean existsByUsername(String username) {
        return ensUserRepository.existsById(username);
    }

    public EnsUser findByIdOrThrow(String username) {
        try {
            return toEnsUser(ensUserRepository.findById(username).orElseThrow());
        } catch (NoSuchElementException e) {
            log.info("findByIdOrThrow executed for username-{}, user doesn't exist", username);
            throw new UserDoesntExistException();
        }
    }

    public EnsUser findByAccountIdOrThrow(String accountId) {
        try {
            return toEnsUser(ensUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
        } catch (NoSuchElementException e) {
            log.info("findByAccountIdOrThrow executed for accountId-{}, user doesn't exist", accountId);
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
