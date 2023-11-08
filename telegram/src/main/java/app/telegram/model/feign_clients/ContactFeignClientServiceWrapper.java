package app.telegram.model.feign_clients;

import app.telegram.users.db.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactFeignClientServiceWrapper {

    private final ContactFeignService contactFeignService;

    private final TelegramUserService telegramUserService;

    public List<Contact> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return contactFeignService.findAllById(securityToken);
    }

    public void addMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactFeignService.addMany(securityToken, contacts);
    }

    public void addOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactFeignService.addOne(securityToken, contact);
    }

    public void removeMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactFeignService.removeMany(securityToken, contacts);
    }

    public void removeOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactFeignService.removeOne(securityToken, contact);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        contactFeignService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return telegramUserService.getSecurityToken(chatId);
    }
}
