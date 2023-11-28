package app.notification.model.db;

import app.notification.model.Notification;
import app.utils.feign_clients.notification.exceptions.NotificationDoesntExistException;
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
        return toNotification(repository.save(toEntity(notification)));
    }

    public boolean doesNotificationExist(Notification notification) {
        return repository.existsById(new NotificationCompositeKey(notification.getAccountId(), notification.getName()));
    }

    public Notification findByIdOrThrow(String accountId, String name) {
        NotificationCompositeKey key = new NotificationCompositeKey(accountId, name);
        try {
            return toNotification(repository.findById(key).orElseThrow());
        } catch (NoSuchElementException e) {
            log.info("findByIdOrThrow executed for accountId-{} and name-{}, notification doesnt exist", accountId, name);
            throw new NotificationDoesntExistException();
        }
    }

    public List<Notification> findAllByAccountId(String accountId) {
        List<Notification> result =
                repository.findAllByAccountId(accountId).stream().map(this::toNotification).toList();
        return result;
    }

    public List<Notification> findAll() {
        return repository.findAll().stream().map(this::toNotification).toList();
    }

    public void delete(Notification notification) {
        repository.delete(toEntity(notification));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteAll(List<Notification> notifications) {
        List<NotificationEntity> entitiesToDelete = notifications.stream().map(this::toEntity).toList();
        repository.deleteAll(entitiesToDelete);
    }

    private NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity(notification.getAccountId(), notification.getName(), notification.getText());
    }

    private Notification toNotification(NotificationEntity entity) {
        return new Notification(entity);
    }
}
