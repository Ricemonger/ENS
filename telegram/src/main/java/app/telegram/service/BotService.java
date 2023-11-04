package app.telegram.service;

import app.telegram.security.TelegramUserService;
import app.telegram.service.contact.ContactServiceWrapper;
import app.telegram.service.notification.NotificationServiceWrapper;
import app.telegram.service.sender.SendService;
import app.utils.contact.Contact;
import app.utils.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    private final ContactServiceWrapper contactService;

    private final NotificationServiceWrapper notificationService;

    private final SendService sendService;

    public void create(Long chatId) {
        telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void sendAll(Long chatId) {
        sendService.sendAll(chatId);
    }

    public void unlink(Long chatId) {
        telegramUserService.unlink(chatId);
    }

    public void link(Long chatId, String username, String password) {
        telegramUserService.link(chatId, username, password);
    }

    public String getUserData(Long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Notification> notificationList = notificationService.findAll(chatId);
        List<Contact> contactList = contactService.findAll(chatId);
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
        notificationService.clear(chatId);
        contactService.clear(chatId);
    }

    public boolean isLinked(Long chatId) {
        return telegramUserService.isLinked(chatId);
    }

    public void addManyContacts(List<Contact> contacts) {
        contactService.addMany(contacts);
    }

    public void addOneContact(Contact contact) {
        contactService.addOne(contact);
    }

    public void removeManyContacts(List<Contact> contacts) {
        contactService.removeMany(contacts);
    }

    public void removeOneContact(Contact contact) {
        contactService.removeOne(contact);
    }

    public void addManyNotifications(List<Notification> notifications) {
        notificationService.addMany(notifications);
    }

    public void addOneNotification(Notification notification) {
        notificationService.addOne(notification);
    }

    public void removeManyNotifications(List<Notification> notifications) {
        notificationService.removeMany(notifications);
    }

    public void removeOneNotifications(Notification notification) {
        notificationService.removeOne(notification);
    }
}
