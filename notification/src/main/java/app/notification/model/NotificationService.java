package app.notification.model;

import app.notification.model.db.NotificationRepositoryService;
import app.utils.feign_clients.notification.exceptions.NotificationAlreadyExistsException;
import app.utils.feign_clients.notification.exceptions.NotificationDoesntExistException;
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
        try {
            getByKeyOrThrow(notification);
        } catch (NotificationDoesntExistException e) {
            log.trace("create method was executed with params: {}", notification);
            return repositoryService.save(notification);
        }
        throw new NotificationAlreadyExistsException();
    }

    public Notification update(Notification notification) {
        Notification dbNotification = getByKeyOrThrow(notification);
        dbNotification.setText(notification.getText());
        log.trace("update method was executed with params: {}", notification);
        return repositoryService.save(dbNotification);
    }

    public Notification delete(Notification notification) {
        Notification dbNotification = getByKeyOrThrow(notification);
        repositoryService.delete(dbNotification);
        log.trace("delete method was executed with params: {}", notification);
        return dbNotification;
    }

    public void clear(String accountId) {
        List<Notification> notifications = repositoryService.findAllByAccountId(accountId);
        repositoryService.deleteAll(notifications);
        log.trace("clear method was executed with accountId: {}", accountId);
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Notification> oldNotifications = findAllByAccountId(oldAccountId);
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
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
        List<Notification> result = byAccountId
                .stream()
                .filter(notification -> notification.getName().startsWith(notificationName))
                .toList();
        log.trace("findAllByPrimaryKey method was executed with params: accountId-{}, notificationName-{} and result:{}", accountId, notificationName, result);
        return result;
    }

    public List<Notification> findAllByAccountId(String accountId) {
        List<Notification> result = repositoryService.findAllByAccountId(accountId);
        log.trace("findAllByAccountId method was executed with params: accountId-{} and result:{}", accountId, result);
        return result;
    }

    private Notification getByKeyOrThrow(Notification notification) {
        return repositoryService.findByIdOrThrow(notification.getAccountId(), notification.getName());
    }
}
