package app.telegram.bot.feign_clients;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFeignClientServiceWrapper {

    private final NotificationFeignClientService notificationFeignClientService;

    private final TelegramUserService telegramUserService;

    public List<Notification> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return notificationFeignClientService.findAllById(securityToken);
    }

    public void addOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignClientService.addOne(securityToken, notification);
    }

    public void removeMany(Long chatId, List<Notification> notifications) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignClientService.removeMany(securityToken, notifications);
    }

    public void removeOne(Long chatId, Notification notification) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignClientService.removeOne(securityToken, notification);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignClientService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return telegramUserService.getAndPutSecurityToken(chatId);
    }
}
