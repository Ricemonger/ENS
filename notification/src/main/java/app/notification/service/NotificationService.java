package app.notification.service;

import app.notification.controller.exceptions.NotificationAlreadyExistsException;
import app.notification.model.Notification;
import app.notification.model.NotificationCompositeKey;
import app.notification.service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        try {
            notificationRepository.findById(new NotificationCompositeKey(notification.getUsername(), notification.getName())).orElseThrow();
        }catch (NoSuchElementException e) {
            log.trace("create method was executed with params: {}",notification);
            return notificationRepository.save(notification);
        }
        throw new NotificationAlreadyExistsException();
    }

    public Notification update(Notification notification) {
        NotificationCompositeKey key = new NotificationCompositeKey(notification.getUsername(), notification.getName());
        Notification dbNotification = notificationRepository.findById(key).orElseThrow();
        dbNotification.setText(notification.getText());
        log.trace("update method was executed with params: {}",notification);
        return notificationRepository.save(dbNotification);
    }

    public Notification delete(Notification notification) {
        NotificationCompositeKey key = new NotificationCompositeKey(notification.getUsername(), notification.getName());
        Notification dbNotification = notificationRepository.findById(key).orElseThrow();
        notificationRepository.delete(dbNotification);
        log.trace("delete method was executed with params: {}",notification);
        return dbNotification;
    }
    public Notification findOneByPrimaryKey(String username, String notificationName){
        Notification result = notificationRepository.findById(new NotificationCompositeKey(username,notificationName)).orElseThrow();
        log.trace("findOneByPrimaryKey method was executed with params: username-{}, notificationName-{} and result:{}",username,notificationName,result);
        return result;
    }
    public List<Notification> findAllByUsername(String username) {
        List<Notification> result = notificationRepository.findAllByUsername(username);
        log.trace("findAllByUsername method was executed with params: username-{} and result:{}",username,result);
        return result;
    }

    public List<Notification> findAllByPrimaryKey(String username, String notificationName) {
        List<Notification> byUsername = findAllByUsername(username);
        List<Notification> result = byUsername
                .stream()
                .filter(notification -> notification.getName().startsWith(notificationName))
                .toList();
        log.trace("findAllByPrimaryKey method was executed with params: username-{}, notificationName-{} and result:{}",username,notificationName,result);
        return result;
    }

    public Map<String, String> getMap(String username) {
        List<Notification> notifications = notificationRepository.findAllByUsername(username);
        Map<String, String> result = notifications.stream().collect(Collectors.toMap(Notification::getName,Notification::getText));
        log.trace("getMap method was executed with params: username-{} and result:{}",username,result);
        return result;
    }
}
