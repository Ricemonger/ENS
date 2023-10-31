package app.security.user.service.ens_user.db;


import app.security.user.service.ens_user.EnsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnsUserRepository extends JpaRepository<EnsUser, String> {
}
