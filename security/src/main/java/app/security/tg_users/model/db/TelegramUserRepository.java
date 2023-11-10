package app.security.tg_users.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, String> {

    boolean existsByAnyUserEntityAccountId(String accountId);

    Optional<TelegramUserEntity> findByAnyUserEntityAccountId(String accountId);
}
