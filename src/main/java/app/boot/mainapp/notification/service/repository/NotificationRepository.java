package app.boot.mainapp.notification.service.repository;

import app.boot.mainapp.notification.model.Notification;
import app.boot.mainapp.notification.model.NotificationCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, NotificationCompositeKey> {
    List<Notification> findAllByUsername(String username);
}
