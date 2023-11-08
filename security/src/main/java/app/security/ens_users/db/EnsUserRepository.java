package app.security.ens_users.db;


import app.security.ens_users.EnsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnsUserRepository extends JpaRepository<EnsUser, String> {

    Optional<EnsUser> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

}
