package app.telegram.users.db;

import app.telegram.users.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
}
