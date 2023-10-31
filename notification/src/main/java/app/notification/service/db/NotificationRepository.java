package app.notification.service.db;

import app.notification.service.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, NotificationCompositeKey> {
    List<Notification> findAllByAccountId(String accountId);
}
