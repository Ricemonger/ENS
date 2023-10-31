package app.notification.service.db;

import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.service.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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