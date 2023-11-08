package app.telegram.model.feign_clients;

import app.telegram.users.db.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.sender.SendFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendFeignClientServiceWrapper {

    private final SendFeignService sendFeignService;

    private final TelegramUserService telegramUserService;

    public void sendAll(Long chatId) {
        String securityToken = telegramUserService.getSecurityToken(chatId);
        sendFeignService.sendAll(securityToken);
    }

    public void sendOne(Long chatId, Contact contact) {
        String securityToken = telegramUserService.getSecurityToken(chatId);
        sendFeignService.sendOne(securityToken, contact);
    }
}
