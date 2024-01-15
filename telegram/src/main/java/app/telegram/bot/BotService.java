package app.telegram.bot;

import app.telegram.bot.exceptions.internal.EmptyInputMapException;
import app.telegram.bot.exceptions.user.InvalidTaskTypeException;
import app.telegram.bot.feign_client_adapters.ContactFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.NotificationFeignClientServiceAdapter;
import app.telegram.bot.feign_client_adapters.SendFeignClientServiceAdapter;
import app.telegram.bot.task.controller.TaskService;
import app.telegram.bot.task.model.Task;
import app.telegram.bot.task.model.TaskType;
import app.telegram.users.controller.TelegramUserService;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.services.contact.Contact;
import app.utils.services.contact.Method;
import app.utils.services.contact.exceptions.InvalidContactMethodException;
import app.utils.services.notification.Notification;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {

    public static final String TIME_AND_DATE_FORMAT = "HH:mm:ss dd-MM-yyyy";

    private final Map<String, Map<InputState, String>> inputsMap = new HashMap<>();

    private final TelegramUserService telegramUserService;

    private final TaskService taskService;

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
        List<Task> taskList = taskService.findAllByChatId(chatId);
        String accountInfo = telegramUserService.getAccountInfo(chatId);

        stringBuilder.append("Notifications:\n");
        stringBuilder.append(notificationList);
        stringBuilder.append("\nContacts:\n");
        stringBuilder.append(contactList);
        stringBuilder.append("\nTasks:\n");
        stringBuilder.append(taskList);
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

    public void createTask(Long chatId) {
        Task task = getTaskFromInputMap(chatId);
        clearUserInputs(chatId);
        taskService.create(task);
    }

    public void deleteTask(Long chatId) {
        Task task = getTaskKeyFromInputMap(chatId);
        clearUserInputs(chatId);
        taskService.deleteByKey(chatId, task.getName());
    }

    public void deleteAllTasks(Long chatId) {
        taskService.deleteAllByChatId(chatId);
    }

    public String showAllTasks(Long chatId) {
        return taskService.findAllByChatId(chatId).toString();
    }

    public void addContact(Long chatId) {
        Contact contact = getContactFromInputsMap(chatId);
        clearUserInputs(chatId);
        contactFeignClientServiceAdapter.addOne(chatId, contact);
    }

    public void removeOneContact(Long chatId) {
        Contact contact = getContactFromInputsMap(chatId);
        clearUserInputs(chatId);
        contactFeignClientServiceAdapter.removeOne(chatId, contact);
    }

    public void removeManyContacts(Long chatId) {
        Contact contact = getContactFromInputsMap(chatId);
        clearUserInputs(chatId);
        contactFeignClientServiceAdapter.removeMany(chatId, contact);
    }

    public void addNotification(Long chatId) {
        Notification notification = getNotificationFromInputsMap(chatId);
        clearUserInputs(chatId);
        notificationFeignClientServiceAdapter.addOne(chatId, notification);
    }

    public void removeOneNotification(Long chatId) {
        Notification notification = getNotificationFromInputsMap(chatId);
        clearUserInputs(chatId);
        notificationFeignClientServiceAdapter.removeOne(chatId, notification);
    }

    public void removeManyNotifications(Long chatId) {
        Notification notification = getNotificationFromInputsMap(chatId);
        clearUserInputs(chatId);
        notificationFeignClientServiceAdapter.removeMany(chatId, notification);
    }

    public void setCustomPhraseFromInputMap(Long chatId) {
        String customPhrase = getCustomPhraseFromInputMap(chatId);
        clearUserInputs(chatId);
        setUserCustomPhrase(chatId, customPhrase);
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

    public Task getTaskFromInputMap(Long chatId) {
        Map<InputState, String> requestMap = getUserInputMapOrThrow(chatId);
        String name = requestMap.get(InputState.TASK_NAME);
        String type = requestMap.get(InputState.TASK_TYPE);
        String time = requestMap.get(InputState.TASK_TIME);
        String method = requestMap.get(InputState.CONTACT_METHOD);
        String contactId = requestMap.get(InputState.CONTACT_ID);
        String notification = requestMap.get(InputState.NOTIFICATION_TEXT);

        Date timeDated;
        TaskType typed;
        Method methoded;

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_AND_DATE_FORMAT);
        try {
            timeDated = dateFormat.parse(time);
        } catch (ParseException | NullPointerException e) {
            log.info("getTaskFromInputMap throws for chatOd-{}, invalid time input-{}, default time used", chatId, time);
            timeDated = new Date();
            timeDated.setTime(timeDated.getTime() + 3_600_000 * 24);
        }

        try {
            typed = TaskType.valueOf(type);
        } catch (RuntimeException e) {
            throw new InvalidTaskTypeException(e);
        }

        try {
            methoded = Method.valueOf(method);
        } catch (RuntimeException e) {
            throw new InvalidContactMethodException(e);
        }

        return new Task(String.valueOf(chatId), name, timeDated, typed, methoded, contactId, notification);
    }

    public Task getTaskKeyFromInputMap(Long chatId) {
        Map<InputState, String> requestMap = getUserInputMapOrThrow(chatId);
        String name = requestMap.get(InputState.TASK_NAME);
        return new Task(String.valueOf(chatId), name);
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
