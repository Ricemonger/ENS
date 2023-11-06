package app.telegram.service.clients;

import app.telegram.security.db.TelegramUserService;
import app.utils.contact.Contact;
import app.utils.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceWrapper {

    private final ContactService contactService;

    private final TelegramUserService telegramUserService;

    public List<Contact> findAll(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        return contactService.findAllById(securityToken);
    }

    public void addMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactService.AddMany(securityToken, contacts);
    }

    public void addOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactService.AddOne(securityToken, contact);
    }

    public void removeMany(Long chatId, List<Contact> contacts) {
        String securityToken = getSecurityToken(chatId);
        contactService.removeMany(securityToken, contacts);
    }

    public void removeOne(Long chatId, Contact contact) {
        String securityToken = getSecurityToken(chatId);
        contactService.removeOne(securityToken, contact);
    }

    public void clear(Long chatId) {
        String securityToken = getSecurityToken(chatId);
        contactService.removeAllById(securityToken);
    }

    private String getSecurityToken(Long chatId) {
        return telegramUserService.getSecurityToken(chatId);
    }
}
