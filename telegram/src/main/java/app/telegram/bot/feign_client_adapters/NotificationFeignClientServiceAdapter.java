package app.telegram.bot.feign_client_adapters;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFeignClientServiceAdapter {

    private final NotificationFeignClientService notificationFeignClientService;

    private final TelegramUserService telegramUserService;

    public List<Notification> findAll(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        return notificationFeignClientService.findAllById(securityToken);
    }

    public void addOne(Long chatId, Notification notification) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        notificationFeignClientService.addOne(securityToken, notification);
    }

    public void removeOne(Long chatId, Notification notification) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        notificationFeignClientService.removeOne(securityToken, notification);
    }

    public void removeMany(Long chatId, Notification notificationFilters) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        notificationFeignClientService.removeMany(securityToken, notificationFilters);
    }

    public void clear(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        notificationFeignClientService.removeAllById(securityToken);
    }

}
