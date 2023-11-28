package app.notification.model;

import app.notification.model.db.NotificationRepositoryService;
import app.utils.feign_clients.notification.exceptions.NotificationAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepositoryService repositoryService;

    public Notification create(Notification notification) {
        if (!doesNotificationExist(notification)) {
            return repositoryService.save(notification);
        } else {
            log.info("create executed for notification-{}, notification already exists", notification);
            throw new NotificationAlreadyExistsException();
        }
    }

    public Notification update(Notification notification) {
        Notification dbNotification = getByKeyOrThrow(notification);
        dbNotification.setText(notification.getText());
        return repositoryService.save(dbNotification);
    }

    public Notification delete(Notification notification) {
        Notification dbNotification = getByKeyOrThrow(notification);
        repositoryService.delete(dbNotification);
        return dbNotification;
    }

    public void clear(String accountId) {
        repositoryService.deleteAll(repositoryService.findAllByAccountId(accountId));
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Notification> oldNotifications = findAllByAccountId(oldAccountId);
        for (Notification toDelete : oldNotifications) {
            repositoryService.delete(toDelete);
            Notification toSave = new Notification();
            toSave.setAccountId(newAccountId);
            toSave.setText(toDelete.getText());
            toSave.setName(toDelete.getName());
            repositoryService.save(toSave);
        }
    }

    public List<Notification> findAllLikePrimaryKey(String accountId, String notificationName) {
        List<Notification> byAccountId = findAllByAccountId(accountId);
        return byAccountId
                .stream()
                .filter(notification -> notification.getName().startsWith(notificationName))
                .toList();
    }

    public List<Notification> findAllByAccountId(String accountId) {
        return repositoryService.findAllByAccountId(accountId);
    }

    private boolean doesNotificationExist(Notification notification) {
        log.debug("doesNotificationExists called for notification-{}", notification);
        boolean result = repositoryService.doesNotificationExist(notification);
        log.trace("doesNotificationExists called for notification-{} with result-{}", notification, result);
        return result;
    }

    private Notification getByKeyOrThrow(Notification notification) {
        log.debug("getByKeyOrThrow called for notification-{}", notification);
        Notification result = repositoryService.findByIdOrThrow(notification.getAccountId(), notification.getName());
        log.trace("getByKeyOrThrow called for notification-{} with result-{}", notification, result);
        return result;
    }
}
