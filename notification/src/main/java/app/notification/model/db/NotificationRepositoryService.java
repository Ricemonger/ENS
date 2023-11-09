package app.notification.model.db;

import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryService {

    private final NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        try {
            getByKey(notification);
        } catch (NotificationDoesntExistException e) {
            log.trace("create method was executed with params: {}", notification);
            return notificationRepository.save(notification);
        }
        throw new NotificationAlreadyExistsException();
    }

    public Notification update(Notification notification) {
        Notification dbNotification = getByKey(notification);
        dbNotification.setText(notification.getText());
        log.trace("update method was executed with params: {}", notification);
        return notificationRepository.save(dbNotification);
    }

    public Notification delete(Notification notification) {
        Notification dbNotification = getByKey(notification);
        notificationRepository.delete(dbNotification);
        log.trace("delete method was executed with params: {}", notification);
        return dbNotification;
    }

    public void clear(String accountId) {
        List<Notification> notifications = notificationRepository.findAllByAccountId(accountId);
        notificationRepository.deleteAll(notifications);
        log.trace("clear method was executed with accountId: {}", accountId);
    }

    public Notification findOneByPrimaryKey(String accountId, String notificationName) {
        Notification result = getByKey(accountId, notificationName);
        log.trace("findOneByPrimaryKey method was executed with params: accountId-{}, notificationName-{} and result:{}", accountId, notificationName, result);
        return result;
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
        List<Notification> result = notificationRepository.findAllByAccountId(accountId);
        log.trace("findAllByAccountId method was executed with params: accountId-{} and result:{}", accountId, result);
        return result;
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Notification> oldNotifications = findAllByAccountId(oldAccountId);
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        for (Notification notification : oldNotifications) {
            notificationRepository.delete(notification);
            notification.setAccountId(newAccountId);
            notificationRepository.save(notification);
        }
    }

    private Notification getByKey(Notification notification) {
        return getByKey(notification.getAccountId(), notification.getName());
    }

    private Notification getByKey(String accountId, String notificationName) {
        Notification result;
        try {
            result = notificationRepository.findById(new NotificationCompositeKey(accountId, notificationName)).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NotificationDoesntExistException(e);
        }
        return result;
    }
}
