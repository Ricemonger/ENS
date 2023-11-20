package app.telegram.bot;

import app.telegram.bot.feign_clients.ContactFeignClientServiceWrapper;
import app.telegram.bot.feign_clients.NotificationFeignClientServiceWrapper;
import app.telegram.bot.feign_clients.SendFeignClientServiceWrapper;
import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    private final ContactFeignClientServiceWrapper contactFeignClientServiceWrapper;

    private final NotificationFeignClientServiceWrapper notificationFeignClientServiceWrapper;

    private final SendFeignClientServiceWrapper sendFeignClientServiceWrapper;

    public void create(Long chatId) {
        telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void sendAll(Long chatId) {
        sendFeignClientServiceWrapper.sendAll(chatId);
    }

    public void sendOne(Long chatId, Contact contact) {
        sendFeignClientServiceWrapper.sendOne(chatId, contact);
    }

    public void unlink(Long chatId) {
        telegramUserService.unlink(chatId);
    }

    public void link(Long chatId, String username, String password) {
        telegramUserService.link(chatId, username, password);
    }

    public String getUserData(Long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Notification> notificationList = notificationFeignClientServiceWrapper.findAll(chatId);
        List<Contact> contactList = contactFeignClientServiceWrapper.findAll(chatId);
        String accountInfo = telegramUserService.getAccountInfo(chatId);

        stringBuilder.append("Notifications:\n");
        stringBuilder.append(notificationList);
        stringBuilder.append("\nContacts:\n");
        stringBuilder.append(contactList);
        stringBuilder.append("\nAccount info:\n");
        stringBuilder.append(accountInfo);

        return stringBuilder.toString();
    }

    public void removeAllData(Long chatId) {
        clear(chatId);
        telegramUserService.removeAccount(chatId);
    }

    public void clear(Long chatId) {
        notificationFeignClientServiceWrapper.clear(chatId);
        contactFeignClientServiceWrapper.clear(chatId);
    }

    public boolean isLinked(Long chatId) {
        return telegramUserService.isLinked(chatId);
    }

    public void addManyContacts(Long chatId, List<Contact> contacts) {
        contactFeignClientServiceWrapper.addMany(chatId, contacts);
    }

    public void addOneContact(Long chatId, Contact contact) {
        contactFeignClientServiceWrapper.addOne(chatId, contact);
    }

    public void removeManyContacts(Long chatId, List<Contact> contacts) {
        contactFeignClientServiceWrapper.removeMany(chatId, contacts);
    }

    public void removeOneContact(Long chatId, Contact contact) {
        contactFeignClientServiceWrapper.removeOne(chatId, contact);
    }

    public void addManyNotifications(Long chatId, List<Notification> notifications) {
        notificationFeignClientServiceWrapper.addMany(chatId, notifications);
    }

    public void addOneNotification(Long chatId, Notification notification) {
        notificationFeignClientServiceWrapper.addOne(chatId, notification);
    }

    public void removeManyNotifications(Long chatId, List<Notification> notifications) {
        notificationFeignClientServiceWrapper.removeMany(chatId, notifications);
    }

    public void removeOneNotifications(Long chatId, Notification notification) {
        notificationFeignClientServiceWrapper.removeOne(chatId, notification);
    }
}
