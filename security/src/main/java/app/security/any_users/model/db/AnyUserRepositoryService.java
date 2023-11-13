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

    public boolean existsById(String accountId) {
        return anyUserRepository.existsById(accountId);
    }

    public void deleteById(String accountId) {
        anyUserRepository.deleteById(accountId);
    }

    public AnyUser findByIdOrThrow(String accountId) {
        return toAnyUser(anyUserRepository.findById(accountId).orElseThrow());
    }

    public List<AnyUser> findAll() {
        return anyUserRepository.findAll().stream().map(AnyUser::new).toList();
    }

    public AnyUser save(AnyUser anyUser) {
        return toAnyUser(anyUserRepository.save(toAnyUserEntity(anyUser)));
    }

    private AnyUser toAnyUser(AnyUserEntity entity) {
        return new AnyUser(entity);
    }

    private AnyUserEntity toAnyUserEntity(AnyUser anyUser) {
        return new AnyUserEntity(anyUser.getAccountId());
    }

    public void deleteAll() {
        anyUserRepository.deleteAll();
    }
}