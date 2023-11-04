package app.telegram.service.notification;

import app.telegram.service.security.SecurityUserService;
import app.utils.notification.Notification;
import app.utils.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceWrapper {

    private final NotificationService notificationService;

    private final SecurityUserService securityUserService;

    public List<Notification> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return notificationService.findAllById(securityToken);
    }

    public void addMany(Long chatId, List<Notification> notifications) {
        String securityToken = getSecurityToken(chatId);
        notificationService.addMany(securityToken, notifications);
    }

    public void addOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationService.addOne(securityToken, notification);
    }

    public void removeMany(Long chatId, List<Notification> notifications) {
        String securityToken = getSecurityToken(chatId);
        notificationService.removeMany(securityToken, notifications);
    }

    public void removeOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationService.removeOne(securityToken, notification);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        notificationService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return securityUserService.getSecurityToken(chatId);
    }
}
