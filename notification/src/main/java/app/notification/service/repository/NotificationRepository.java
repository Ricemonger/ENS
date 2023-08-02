package app.notification.service.repository;

import app.notification.model.Notification;
import app.notification.model.NotificationCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, NotificationCompositeKey> {
    List<Notification> findAllByUsername(String username);
}
