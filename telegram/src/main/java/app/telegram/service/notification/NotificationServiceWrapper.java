package app.telegram.service.notification;

import app.utils.notification.Notification;
import app.utils.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceWrapper {

    private final NotificationService notificationService;

    public List<Notification> findAll(Long chatId) {


    }

    public void addMany(List<Notification> notifications) {

    }

    public void addOne(Notification notification) {

    }

    public void removeMany(List<Notification> notifications) {

    }

    public void removeOne(Notification notification) {

    }

    public void clear(Long chatId) {

    }
}
