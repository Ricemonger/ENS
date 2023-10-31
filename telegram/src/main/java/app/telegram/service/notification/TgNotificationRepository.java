package app.telegram.service.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TgNotificationRepository extends JpaRepository<TgNotification, TgNotificationCompositeKey> {
}
