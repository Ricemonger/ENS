package app.telegram.bot.feign_clients;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.sender.SendFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendFeignClientServiceWrapper {

    private final SendFeignClientService sendFeignClientService;

    private final TelegramUserService telegramUserService;

    public void sendAll(Long chatId) {
        String securityToken = telegramUserService.getAndPutSecurityToken(chatId);
        sendFeignClientService.sendAll(securityToken);
    }

    public void sendOne(Long chatId, Contact contact) {
        String securityToken = telegramUserService.getAndPutSecurityToken(chatId);
        sendFeignClientService.sendOne(securityToken, contact);
    }
}
