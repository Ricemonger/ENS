package app.notification.model.db;

import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryService {

    private final NotificationRepository repository;


    public Notification save(Notification notification) {
        log.trace("save method is called for notification-{}", notification);
        return toNotification(repository.save(toEntity(notification)));
    }

    public Notification findByIdOrThrow(String accountId, String name) {
        NotificationCompositeKey key = new NotificationCompositeKey(accountId, name);
        try {
            Notification result = toNotification(repository.findById(key).orElseThrow());
            log.trace("findByIdOrThrow was executed for accountId-{}, name-{} and result-{}", accountId, name, result);
            return result;
        } catch (NoSuchElementException e) {
            throw new NotificationDoesntExistException();
        }
    }

    public List<Notification> findAllByAccountId(String accountId) {
        List<Notification> result =
                repository.findAllByAccountId(accountId).stream().map(this::toNotification).toList();
        log.trace("findAllByAccountId was executed for accountId-{} and result-{}", accountId, result);
        return result;
    }

    public List<Notification> findAll() {
        log.trace("findAll method was called");
        return repository.findAll().stream().map(this::toNotification).toList();
    }

    public void delete(Notification notification) {
        repository.delete(toEntity(notification));
        log.trace("delete method was called for notification-{}", notification);
    }

    public void deleteAll(List<Notification> notifications) {
        List<NotificationEntity> entitiesToDelete = notifications.stream().map(this::toEntity).toList();
        repository.deleteAll(entitiesToDelete);
        log.trace("deleteAll method was called for notifications-{}", notifications);
    }

    public void deleteAll() {
        repository.deleteAll();
        log.trace("deleteAll method was called");
    }

    private NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity(notification.getAccountId(), notification.getName(), notification.getText());
    }

    private Notification toNotification(NotificationEntity entity) {
        return new Notification(entity);
    }
}
