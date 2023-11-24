package app.telegram.bot;

import app.telegram.bot.exceptions.ClearingException;
import app.telegram.bot.exceptions.SendingException;
import app.telegram.bot.feign_clients.ContactFeignClientServiceWrapper;
import app.telegram.bot.feign_clients.NotificationFeignClientServiceWrapper;
import app.telegram.bot.feign_clients.SendFeignClientServiceWrapper;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.telegram.users.model.TelegramUserService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {

    private final Map<String, Map<InputState, String>> inputsMap = new HashMap<>();

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
        try {
            sendFeignClientServiceWrapper.sendAll(chatId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendingException();
        }
    }

    public void sendOne(Long chatId, Contact contact) {
        sendFeignClientServiceWrapper.sendOne(chatId, contact);
    }

    public void unlink(Long chatId) {
        telegramUserService.unlink(chatId);
    }

    public void link(Long chatId) {
        String[] s = getUsernameAndPasswordFromInputMap(chatId);
        telegramUserService.link(chatId, s[0], s[1]);
        clearInputs(chatId);
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
        try {
            notificationFeignClientServiceWrapper.clear(chatId);
            contactFeignClientServiceWrapper.clear(chatId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ClearingException();
        }
    }

    public boolean isLinked(Long chatId) {
        return telegramUserService.isLinked(chatId);
    }

    public void addContactFromInputMap(Long chatId) {
        contactFeignClientServiceWrapper.addOne(chatId, getContactFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void removeManyContacts(Long chatId) {
        contactFeignClientServiceWrapper.removeMany(chatId, getContactFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void removeOneContact(Long chatId) {
        contactFeignClientServiceWrapper.removeOne(chatId, getContactFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void addNotificationFromInputMap(Long chatId) {
        notificationFeignClientServiceWrapper.addOne(chatId, getNotificationFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void removeManyNotifications(Long chatId) {
        notificationFeignClientServiceWrapper.removeMany(chatId, getNotificationFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void removeOneNotification(Long chatId) {
        notificationFeignClientServiceWrapper.removeOne(chatId, getNotificationFromInputsMap(chatId));
        clearInputs(chatId);
    }

    public void setNextInputGroup(Long chatId, InputGroup inputGroup) {
        telegramUserService.setNextInputGroup(chatId, inputGroup);
    }

    public void setNextInput(Long chatId, InputState inputState) {
        telegramUserService.setNextInput(chatId, inputState);
    }

    public void saveInput(Long chatId, InputState inputState, String inputText) {
        String keyChatId = String.valueOf(chatId);

        Map<InputState, String> oldInput = inputsMap.get(keyChatId);

        if (oldInput == null) {
            oldInput = new HashMap<>();
        }

        oldInput.put(inputState, inputText);

        inputsMap.put(keyChatId, oldInput);
    }

    public void clearInputs(Long chatId) {
        inputsMap.put(String.valueOf(chatId), new HashMap<>());
    }

    public InputGroup geUserInputGroup(Long chatId) {
        return telegramUserService.getInputGroup(chatId);
    }

    public InputState geUserInputState(Long chatId) {
        return telegramUserService.getInputState(chatId);
    }

    public Contact getContactFromInputsMap(Long chatId) {
        Map<InputState, String> contactMap = inputsMap.get(String.valueOf(chatId));

        Method method = Method.valueOf(contactMap.get(InputState.CONTACT_METHOD).trim().toUpperCase(Locale.ROOT));
        String contactId = contactMap.get(InputState.CONTACT_CONTACT_ID);
        String notificationName = contactMap.get(InputState.CONTACT_NOTIFICATION_NAME);

        return new Contact(method, contactId, notificationName);
    }

    public Notification getNotificationFromInputsMap(Long chatId) {
        Map<InputState, String> notificationMap = inputsMap.get(String.valueOf(chatId));

        String name = notificationMap.get(InputState.NOTIFICATION_NAME);
        String text = notificationMap.get(InputState.NOTIFICATION_TEXT);

        return new Notification(name, text);
    }

    public String[] getUsernameAndPasswordFromInputMap(Long chatId) {
        Map<InputState, String> userInputsMap = inputsMap.get(String.valueOf(chatId));
        String username = userInputsMap.get(InputState.LINK_USERNAME);
        String password = userInputsMap.get(InputState.LINK_PASSWORD);
        return new String[]{username, password};
    }
}
