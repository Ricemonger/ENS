package app.telegram.bot.feign_client_wrappers;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFeignClientServiceAdapter {

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

    public void removeMany(Long chatId, Notification notificationFilters) {
        String securityToken = getSecurityToken(chatId);
        notificationFeignClientService.removeMany(securityToken, notificationFilters);
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
        log.debug("getSecurityToken called for chatId-{}", chatId);
        String result = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        log.trace("getSecurityToken executed for chatId-{} and result-{}", chatId, result);
        return result;
    }
}
