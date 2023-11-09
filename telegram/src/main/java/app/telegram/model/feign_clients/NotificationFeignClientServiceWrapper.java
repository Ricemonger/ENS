package app.telegram.model.feign_clients;

import app.telegram.users.db.TelegramUserService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFeignClientServiceWrapper {

    private final NotificationFeignService notificationFeignService;

    private final TelegramUserService telegramUserService;

    public List<Notification> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return notificationFeignService.findAllById(securityToken);
    }

    public void addMany(Long chatId, List<Notification> notifications) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignService.addMany(securityToken, notifications);
    }

    public void addOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignService.addOne(securityToken, notification);
    }

    public void removeMany(Long chatId, List<Notification> notifications) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignService.removeMany(securityToken, notifications);
    }

    public void removeOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignService.removeOne(securityToken, notification);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return telegramUserService.getSecurityToken(chatId);
    }
}
