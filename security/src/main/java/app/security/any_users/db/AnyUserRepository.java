package app.security.any_users.db;

import app.security.any_users.AnyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnyUserRepository extends JpaRepository<AnyUser, String> {
}
