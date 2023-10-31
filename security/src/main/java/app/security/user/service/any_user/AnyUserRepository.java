package app.security.user.service.any_user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnyUserRepository extends JpaRepository<AnyUser, String> {
}
