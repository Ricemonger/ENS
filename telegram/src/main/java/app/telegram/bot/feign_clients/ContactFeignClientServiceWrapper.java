package app.telegram.bot.feign_clients;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactFeignClientServiceWrapper {

    private final ContactFeignClientService contactFeignClientService;

    private final TelegramUserService telegramUserService;

    public List<Contact> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return contactFeignClientService.findAllById(securityToken);
    }

    public void addMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactFeignClientService.addMany(securityToken, contacts);
    }

    public void addOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactFeignClientService.addOne(securityToken, contact);
    }

    public void removeMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactFeignClientService.removeMany(securityToken, contacts);
    }

    public void removeOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactFeignClientService.removeOne(securityToken, contact);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        contactFeignClientService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return telegramUserService.getAndPutSecurityToken(chatId);
    }
}
