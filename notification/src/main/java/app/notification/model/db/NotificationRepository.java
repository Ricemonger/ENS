package app.notification.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, NotificationCompositeKey> {
    List<NotificationEntity> findAllByAccountId(String accountId);
}
