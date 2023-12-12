package app.telegram.bot;

import app.telegram.bot.exceptions.internal.EmptyInputMapException;
import app.telegram.bot.feign_client_adapters.ContactFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.NotificationFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.SendFeignClientServiceAdapter;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.telegram.users.model.TelegramUserService;
import app.utils.services.contact.Contact;
import app.utils.services.contact.Method;
import app.utils.services.contact.exceptions.InvalidContactMethodException;
import app.utils.services.notification.Notification;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
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

    private final ContactFeignClientServiceAdapter contactFeignClientServiceAdapter;

    private final NotificationFeignClientServiceAdapter notificationFeignClientServiceAdapter;

    private final SendFeignClientServiceAdapter sendFeignClientServiceAdapter;

    public void create(Long chatId) {
        telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public String getUserData(Long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Notification> notificationList = notificationFeignClientServiceAdapter.findAll(chatId);
        List<Contact> contactList = contactFeignClientServiceAdapter.findAll(chatId);
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
        notificationFeignClientServiceAdapter.clear(chatId);
        contactFeignClientServiceAdapter.clear(chatId);
    }

    public void link(Long chatId) {
        String[] s = getUsernameAndPasswordFromInputsMap(chatId);
        telegramUserService.link(chatId, s[0], s[1]);
        clearUserInputs(chatId);
    }

    public void unlink(Long chatId) {
        telegramUserService.unlink(chatId);
    }

    public boolean isLinked(Long chatId) {
        return telegramUserService.isLinked(chatId);
    }

    public void sendOne(Long chatId) {
        sendFeignClientServiceAdapter.sendOne(chatId, getSendOneRequestFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void sendMany(Long chatId) {
        sendFeignClientServiceAdapter.sendMany(chatId, getSendManyRequestFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void sendAll(Long chatId) {
        sendFeignClientServiceAdapter.sendAll(chatId);
    }

    public void addContact(Long chatId) {
        contactFeignClientServiceAdapter.addOne(chatId, getContactFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void removeOneContact(Long chatId) {
        contactFeignClientServiceAdapter.removeOne(chatId, getContactFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void removeManyContacts(Long chatId) {
        contactFeignClientServiceAdapter.removeMany(chatId, getContactFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void addNotification(Long chatId) {
        notificationFeignClientServiceAdapter.addOne(chatId, getNotificationFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void removeOneNotification(Long chatId) {
        notificationFeignClientServiceAdapter.removeOne(chatId, getNotificationFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void removeManyNotifications(Long chatId) {
        notificationFeignClientServiceAdapter.removeMany(chatId, getNotificationFromInputsMap(chatId));
        clearUserInputs(chatId);
    }

    public void setCustomPhraseFromInputMap(Long chatId) {
        setUserCustomPhrase(chatId, getCustomPhraseFromInputMap(chatId));
        clearUserInputs(chatId);
    }

    public void cancelInputs(Long chatId) {
        setUserNextInputState(chatId, InputState.BASE);
        setUserNextInputGroup(chatId, InputGroup.BASE);
        clearUserInputs(chatId);
    }

    public void setBaseInputAndGroupForAllUsers() {
        telegramUserService.setBaseInputAndGroupForAllUsers();
    }

    public void setUserNextInputGroup(Long chatId, InputGroup inputGroup) {
        telegramUserService.setInputGroup(chatId, inputGroup);
    }

    public InputGroup getUserNextInputGroupOrBase(Long chatId) {
        return telegramUserService.getInputGroupOrBase(chatId);
    }

    public void setUserNextInputState(Long chatId, InputState inputState) {
        telegramUserService.setInputState(chatId, inputState);
    }

    public InputState getUserNextInputStateOrBase(Long chatId) {
        return telegramUserService.getInputStateOrBase(chatId);
    }

    public void setUserActionConfirmFlag(Long chatId, boolean flag) {
        telegramUserService.setActionConfirmFlag(chatId, flag);
    }

    public boolean getUserActionConfirmFlag(Long chatId) {
        return telegramUserService.getActionConfirmFlag(chatId);
    }

    public void setUserCustomPhrase(Long chatId, String customPhrase) {
        telegramUserService.setCustomPhrase(chatId, customPhrase);
    }

    public String getUserCustomPhrase(Long chatId) {
        return telegramUserService.getCustomPhrase(chatId);
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

    public void clearUserInputs(Long chatId) {
        inputsMap.put(String.valueOf(chatId), new HashMap<>());
    }

    public Contact getContactFromInputsMap(Long chatId) {
        Map<InputState, String> contactMap = getUserInputMapOrThrow(chatId);
        try {
            Method method = Method.valueOf(contactMap.get(InputState.CONTACT_METHOD).trim().toUpperCase(Locale.ROOT));
            String contactId = contactMap.get(InputState.CONTACT_ID);
            String notificationName = contactMap.get(InputState.NOTIFICATION_NAME);
            return new Contact(method, contactId, notificationName);
        } catch (RuntimeException e) {
            log.info("getContactFromInputsMap throws for chatId-{}, invalid contact method name", chatId);
            throw new InvalidContactMethodException();
        }
    }

    public Notification getNotificationFromInputsMap(Long chatId) {
        Map<InputState, String> notificationMap = getUserInputMapOrThrow(chatId);

        String name = notificationMap.get(InputState.NOTIFICATION_NAME);
        String text = notificationMap.get(InputState.NOTIFICATION_TEXT);

        return new Notification(name, text);
    }

    public String[] getUsernameAndPasswordFromInputsMap(Long chatId) {
        Map<InputState, String> userInputsMap = getUserInputMapOrThrow(chatId);

        String username = userInputsMap.get(InputState.USERNAME);
        String password = userInputsMap.get(InputState.PASSWORD);

        return new String[]{username, password};
    }

    public SendOneRequest getSendOneRequestFromInputsMap(Long chatId) {
        Map<InputState, String> requestMap = getUserInputMapOrThrow(chatId);

        String method = requestMap.get(InputState.CONTACT_METHOD);
        String contactId = requestMap.get(InputState.CONTACT_ID);
        String notificationText = requestMap.get(InputState.NOTIFICATION_TEXT);

        return new SendOneRequest(method, contactId, notificationText);
    }

    public SendManyRequest getSendManyRequestFromInputsMap(Long chatId) {
        Map<InputState, String> requestMap = getUserInputMapOrThrow(chatId);

        String method = requestMap.get(InputState.CONTACT_METHOD);
        String contactId = requestMap.get(InputState.CONTACT_ID);
        String notificationName = requestMap.get(InputState.NOTIFICATION_NAME);

        return new SendManyRequest(method, contactId, notificationName);
    }

    public String getCustomPhraseFromInputMap(Long chatId) {
        Map<InputState, String> requestMap = getUserInputMapOrThrow(chatId);

        String customPhrase = requestMap.get(InputState.CUSTOM_PHRASE);

        return customPhrase;
    }

    public Map<InputState, String> getUserInputMapOrThrow(Long chatId) {
        Map<InputState, String> inputMap = inputsMap.get(String.valueOf(chatId));
        if (inputMap == null || inputMap.isEmpty()) {
            log.info("getInputMapOrThrow throws for chatId-{}, empty chatId input map", chatId);
            throw new EmptyInputMapException();
        }
        return inputMap;
    }


}
