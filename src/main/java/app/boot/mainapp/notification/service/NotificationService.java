package app.boot.mainapp.notification.service;

import app.boot.mainapp.notification.controller.NotificationAlreadyExistsException;
import app.boot.mainapp.notification.model.Notification;
import app.boot.mainapp.notification.model.NotificationCompositeKey;
import app.boot.mainapp.notification.service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
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
    private final NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        try {
            notificationRepository.findById(new NotificationCompositeKey(notification.getUsername(), notification.getName())).orElseThrow();
        }catch (NoSuchElementException e) {
            return notificationRepository.save(notification);
        }
        throw new NotificationAlreadyExistsException();
    }

    public Notification update(Notification notification) {
        NotificationCompositeKey key = new NotificationCompositeKey(notification.getUsername(), notification.getName());
        Notification dbNotification = notificationRepository.findById(key).orElseThrow();
        dbNotification.setText(notification.getText());
        return notificationRepository.save(dbNotification);
    }

    public Notification delete(Notification notification) {
        NotificationCompositeKey key = new NotificationCompositeKey(notification.getUsername(), notification.getName());
        Notification dbNotification = notificationRepository.findById(key).orElseThrow();
        notificationRepository.delete(dbNotification);
        return dbNotification;
    }
    public Notification findOneByPrimaryKey(String username, String notificationName){
        return notificationRepository.findById(new NotificationCompositeKey(username,notificationName)).orElseThrow();
    }
    public List<Notification> findAllByUsername(String username) {
        return notificationRepository.findAllByUsername(username);
    }

    public List<Notification> findAllByPrimaryKey(String username, String notificationName) {
        List<Notification> byUsername = findAllByUsername(username);
        return byUsername
                .stream()
                .filter(notification -> notification.getName().startsWith(notificationName))
                .toList();
    }

    public Map<String, String> getMap(String username) {
        List<Notification> notifications = notificationRepository.findAllByUsername(username);
        return notifications.stream().collect(Collectors.toMap(Notification::getName,Notification::getText));
    }
}
