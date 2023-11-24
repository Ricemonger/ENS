package app.security.any_users.model.db;

import app.security.any_users.AnyUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnyUserRepositoryService {

    private final AnyUserRepository anyUserRepository;

    public AnyUser save(AnyUser anyUser) {
        AnyUser result = toAnyUser(anyUserRepository.save(toAnyUserEntity(anyUser)));
        log.trace("save was called for anyUser-{}, result-{}", anyUser, result);
        return result;
    }

    public boolean existsById(String accountId) {
        boolean result = anyUserRepository.existsById(accountId);
        log.trace("existsById was called for accountId-{}, result-{}", accountId, result);
        return result;
    }

    public void deleteById(String accountId) {
        log.trace("deleteById was called for accountId-{}", accountId);
        anyUserRepository.deleteById(accountId);
    }

    public void deleteAll() {
        log.trace("deleteAll was called");
        anyUserRepository.deleteAll();
    }

    public AnyUser findByIdOrThrow(String accountId) {
        AnyUser result = toAnyUser(anyUserRepository.findById(accountId).orElseThrow());
        log.trace("findByIdOrThrow was called for accountId-{}, result-{}", accountId, result);
        return result;
    }

    public List<AnyUser> findAll() {
        log.trace("findAll was called");
        return anyUserRepository.findAll().stream().map(AnyUser::new).toList();
    }

    private AnyUser toAnyUser(AnyUserEntity entity) {
        return new AnyUser(entity);
    }

    private AnyUserEntity toAnyUserEntity(AnyUser anyUser) {
        return new AnyUserEntity(anyUser.getAccountId());
    }
}