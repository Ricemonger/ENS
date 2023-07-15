package app.boot.notification.service.repository;

import app.boot.notification.model.Notification;
import app.boot.notification.model.NotificationCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, NotificationCompositeKey> {
    List<Notification> findAllByUsername(String username);
}
