package app.security.ens_users.model.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnsUserRepository extends JpaRepository<EnsUserEntity, String> {

    Optional<EnsUserEntity> findByAnyUserEntityAccountId(String accountId);

    boolean existsByAnyUserEntityAccountId(String accountId);
}
