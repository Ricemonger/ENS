package app.notification.model.db;

import app.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, NotificationCompositeKey> {
    List<Notification> findAllByAccountId(String accountId);
}
