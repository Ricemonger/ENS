package app.telegram.security.db;

import app.telegram.security.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
}
