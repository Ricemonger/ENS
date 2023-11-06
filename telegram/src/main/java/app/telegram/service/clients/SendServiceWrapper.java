package app.telegram.service.clients;

import app.telegram.security.db.TelegramUserService;
import app.utils.contact.Contact;
import app.utils.sender.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendServiceWrapper {

    private final SendService sendService;

    private final TelegramUserService telegramUserService;

    public void sendAll(Long chatId) {
        String securityToken = telegramUserService.getSecurityToken(chatId);
        sendService.sendAll(securityToken);
    }

    public void sendOne(Long chatId, Contact contact) {
        String securityToken = telegramUserService.getSecurityToken(chatId);
        sendService.sendOne(securityToken, contact);
    }
}
