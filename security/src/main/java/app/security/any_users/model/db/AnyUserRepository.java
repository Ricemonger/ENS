package app.security.any_users.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnyUserRepository extends JpaRepository<AnyUserEntity, String> {
}
