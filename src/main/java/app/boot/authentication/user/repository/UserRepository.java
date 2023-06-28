package app.boot.authentication.user.repository;

import app.boot.authentication.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
