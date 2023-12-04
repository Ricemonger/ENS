package app.telegram.bot.feign_client_adapters;

import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactFeignClientServiceAdapter {

    private final ContactFeignClientService contactFeignClientService;

    private final TelegramUserService telegramUserService;

    public List<Contact> findAll(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        return contactFeignClientService.findAllById(securityToken);
    }

    public void addOne(Long chatId, Contact contact) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        contactFeignClientService.addOne(securityToken, contact);
    }

    public void removeOne(Long chatId, Contact contact) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        contactFeignClientService.removeOne(securityToken, contact);
    }

    public void removeMany(Long chatId, Contact contactFilters) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        contactFeignClientService.removeMany(securityToken, contactFilters);
    }

    public void clear(Long chatId) {
        String securityToken = telegramUserService.findSecurityTokenOrGenerateAndPut(chatId);
        contactFeignClientService.removeAllById(securityToken);
    }
}
