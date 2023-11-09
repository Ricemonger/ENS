package app.security.tg_users.db;

import app.security.tg_users.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {

    boolean existsByAccountId(String accountId);

    Optional<TelegramUser> findByAccountId(String accountId);
}
