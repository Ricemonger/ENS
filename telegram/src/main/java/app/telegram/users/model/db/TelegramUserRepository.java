package app.telegram.users.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, String> {
}
