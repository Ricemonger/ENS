package app.telegram.service;

import app.telegram.security.db.TelegramUserService;
import app.telegram.service.clients.ContactServiceWrapper;
import app.telegram.service.clients.NotificationServiceWrapper;
import app.telegram.service.clients.SendServiceWrapper;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    private final ContactServiceWrapper contactServiceWrapper;

    private final NotificationServiceWrapper notificationServiceWrapper;

    private final SendServiceWrapper sendServiceWrapper;

    public void create(Long chatId) {
        telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void sendAll(Long chatId) {
        sendServiceWrapper.sendAll(chatId);
    }

    public void sendOne(Long chatId, Contact contact) {
        sendServiceWrapper.sendOne(chatId, contact);
    }

    public void unlink(Long chatId) {
        telegramUserService.unlink(chatId);
    }

    public void link(Long chatId, String username, String password) {
        telegramUserService.link(chatId, username, password);
    }

    public String getUserData(Long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Notification> notificationList = notificationServiceWrapper.findAll(chatId);
        List<Contact> contactList = contactServiceWrapper.findAll(chatId);
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
        notificationServiceWrapper.clear(chatId);
        contactServiceWrapper.clear(chatId);
    }

    public boolean isLinked(Long chatId) {
        return telegramUserService.isLinked(chatId);
    }

    public void addManyContacts(Long chatId, List<Contact> contacts) {
        contactServiceWrapper.addMany(chatId, contacts);
    }

    public void addOneContact(Long chatId, Contact contact) {
        contactServiceWrapper.addOne(chatId, contact);
    }

    public void removeManyContacts(Long chatId, List<Contact> contacts) {
        contactServiceWrapper.removeMany(chatId, contacts);
    }

    public void removeOneContact(Long chatId, Contact contact) {
        contactServiceWrapper.removeOne(chatId, contact);
    }

    public void addManyNotifications(Long chatId, List<Notification> notifications) {
        notificationServiceWrapper.addMany(chatId, notifications);
    }

    public void addOneNotification(Long chatId, Notification notification) {
        notificationServiceWrapper.addOne(chatId, notification);
    }

    public void removeManyNotifications(Long chatId, List<Notification> notifications) {
        notificationServiceWrapper.removeMany(chatId, notifications);
    }

    public void removeOneNotifications(Long chatId, Notification notification) {
        notificationServiceWrapper.removeOne(chatId, notification);
    }
}
