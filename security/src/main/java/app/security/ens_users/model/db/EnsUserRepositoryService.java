package app.security.ens_users.model.db;

import app.security.ens_users.EnsUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EnsUserRepositoryService {

    private final EnsUserRepository ensUserRepository;

    public EnsUser save(EnsUser ensUser) {
        return toEnsUser(ensUserRepository.save(toEnsUserEntity(ensUser)));
    }

    public boolean existsByAccountId(String accountId) {
        return ensUserRepository.existsByAnyUserEntityAccountId(accountId);
    }

    public EnsUser findByIdOrThrow(String username) {
        return toEnsUser(ensUserRepository.findById(username).orElseThrow());
    }

    public EnsUser findByAccountIdOrThrow(String accountId) {
        return toEnsUser(ensUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
    }

    private EnsUser toEnsUser(EnsUserEntity entity) {
        return new EnsUser(entity.getAccountId(), entity.getUsername(), entity.getPassword());
    }

    private EnsUserEntity toEnsUserEntity(EnsUser ensUser) {
        return new EnsUserEntity(ensUser.getAccountId(), ensUser.getUsername(), ensUser.getPassword());
    }
}
