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
import app.utils.services.contact.feign.ContactFeignClientService;
import app.utils.services.contact.feign.MockContactFeignClient;
import app.utils.services.notification.Notification;
import app.utils.services.notification.feign.MockNotificationFeignClient;
import app.utils.services.notification.feign.NotificationFeignClientService;
import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import app.utils.services.sender.feign.MockSendFeignClient;
import app.utils.services.sender.feign.SendFeignClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BotServiceTests {

    private static final Long CHAT_ID = 11111L;

    private static final Long ANOTHER_CHAT_ID = 99999L;

    private static final String STRING_1 = "STRING_1";

    private static final String STRING_2 = "STRING_2";

    private static final String STRING_3 = "STRING_3";

    private static final Method METHOD = Method.SMS;

    private static final InputGroup IG = InputGroup.LINK;

    private static final InputState IS = InputState.CONTACT_ID;

    @MockBean
    private TelegramUserService telegramUserService;

    @MockBean
    private TaskService taskService;

    private ContactFeignClientServiceAdapter contacts;

    private NotificationFeignClientServiceAdapter notifications;

    private SendFeignClientServiceAdapter sends;

    private BotService botService;

    @BeforeEach
    public void setUp() {
        ContactFeignClientServiceAdapter contactFeignClientServiceAdapter =
                new ContactFeignClientServiceAdapter(new ContactFeignClientService(new MockContactFeignClient()), telegramUserService);

        NotificationFeignClientServiceAdapter notificationFeignClientServiceAdapter =
                new NotificationFeignClientServiceAdapter(new NotificationFeignClientService(new MockNotificationFeignClient()), telegramUserService);

        SendFeignClientServiceAdapter sendFeignClientServiceAdapter =
                new SendFeignClientServiceAdapter(new SendFeignClientService(new MockSendFeignClient()), telegramUserService);

        contacts = spy(contactFeignClientServiceAdapter);
        notifications = spy(notificationFeignClientServiceAdapter);
        sends = spy(sendFeignClientServiceAdapter);

        botService = spy(new BotService(telegramUserService, taskService, contacts, notifications, sends));
    }

    @Test
    public void create() {
        botService.create(CHAT_ID);

        verify(telegramUserService).create(CHAT_ID);
    }

    @Test
    public void doesUserExists() {
        when(telegramUserService.doesUserExist(CHAT_ID)).thenReturn(true);

        assertTrue(botService.doesUserExist(CHAT_ID));

        verify(telegramUserService).doesUserExist(CHAT_ID);
    }

    @Test
    public void getUserData() {
        botService.getUserData(CHAT_ID);

        verify(contacts).findAll(CHAT_ID);
        verify(notifications).findAll(CHAT_ID);
        verify(telegramUserService).getAccountInfo(CHAT_ID);
    }

    @Test
    public void removeAllData() {
        botService.removeAllData(CHAT_ID);

        verify(contacts).clear(CHAT_ID);
        verify(notifications).clear(CHAT_ID);
        verify(telegramUserService).removeAccount(CHAT_ID);
    }

    @Test
    public void clear() {
        botService.clear(CHAT_ID);

        verify(contacts).clear(CHAT_ID);
        verify(notifications).clear(CHAT_ID);
    }

    @Test
    public void linkShouldCallUserServiceWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.USERNAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.PASSWORD, STRING_2);

        botService.link(CHAT_ID);

        verify(telegramUserService).link(CHAT_ID, STRING_1, STRING_2);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void linkShouldThrowIfEmptyInputMap() {
        Executable executable = () -> botService.link(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void unlink() {
        botService.unlink(CHAT_ID);

        verify(telegramUserService).unlink(CHAT_ID);
    }

    @Test
    public void isLinked() {
        botService.isLinked(CHAT_ID);

        verify(telegramUserService).isLinked(CHAT_ID);
    }

    @Test
    public void sendOneShouldCallSenderSendOneWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_3);
        SendOneRequest request = new SendOneRequest(STRING_1, STRING_2, STRING_3);

        botService.sendOne(CHAT_ID);

        verify(sends).sendOne(CHAT_ID, request);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void sendOneShouldThrowIfEmptyInputMap() {
        Executable executable = () -> botService.sendOne(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void sendManyShouldCallSenderSendManyWithValuesFromInputMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);
        SendManyRequest request = new SendManyRequest(STRING_1, STRING_2, STRING_3);

        botService.sendMany(CHAT_ID);

        verify(sends).sendMany(CHAT_ID, request);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void sendManyShouldThrowIfEmptyInputMap() {
        Executable executable = () -> botService.sendMany(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void sendAll() {
        botService.sendAll(CHAT_ID);

        verify(sends).sendAll(CHAT_ID);
    }

    @Test
    public void showAllTasks() {
        botService.showAllTasks(CHAT_ID);

        verify(taskService).findAllByChatId(CHAT_ID);
    }

    @Test
    public void getTaskFromInputMapShouldReturnFromMap() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BotService.TIME_AND_DATE_FORMAT);
        Date date = new Date(0);
        try {
            date = simpleDateFormat.parse("10:22:59 04-10-2024");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task TASK = new Task(
                String.valueOf(CHAT_ID), "name",
                date, TaskType.ONE,
                Method.SMS, "phoneNumber", "notificationText");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());
        botService.saveInput(CHAT_ID, InputState.TASK_TIME, "10:22:59 04-10-2024");
        botService.saveInput(CHAT_ID, InputState.TASK_TYPE, TASK.getTaskType().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, TASK.getContactMethod().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, TASK.getContactId());
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, TASK.getContactNotification());

        assertEquals(TASK, botService.getTaskFromInputMap(CHAT_ID));
    }

    @Test
    public void getTaskFromInputMapShouldUseDefaultTimeIfInvalidTimeFormat() {
        Date expectedDate = new Date(new Date().getTime() + 3_600_000 * 24);

        Task TASK = new Task(
                String.valueOf(CHAT_ID), "name",
                expectedDate, TaskType.ONE,
                Method.SMS, "phoneNumber", "notificationText");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());
        botService.saveInput(CHAT_ID, InputState.TASK_TIME, "10-22-59 04-10-2024");//invalid format
        botService.saveInput(CHAT_ID, InputState.TASK_TYPE, TASK.getTaskType().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, TASK.getContactMethod().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, TASK.getContactId());
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, TASK.getContactNotification());

        Task result = botService.getTaskFromInputMap(CHAT_ID);

        assertTrue(Math.abs(result.getTaskTime().compareTo(expectedDate)) < 1000);

        TASK.setTaskTime(result.getTaskTime());

        assertEquals(TASK, result);
    }

    @Test
    public void getTaskFromInputMapShouldThrowInvalidTaskTypeExceptionIfInvalidTaskType() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BotService.TIME_AND_DATE_FORMAT);
        Date date = new Date(0);
        try {
            date = simpleDateFormat.parse("10:22:59 04-10-2024");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task TASK = new Task(
                String.valueOf(CHAT_ID), "name",
                date, TaskType.ONE,
                Method.SMS, "phoneNumber", "notificationText");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());
        botService.saveInput(CHAT_ID, InputState.TASK_TIME, "10:22:59 04-10-2024");
        botService.saveInput(CHAT_ID, InputState.TASK_TYPE, TASK.getTaskType().toString() + " ");
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, TASK.getContactMethod().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, TASK.getContactId());
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, TASK.getContactNotification());

        Executable executable = () -> {
            botService.getTaskFromInputMap(CHAT_ID);
        };

        assertThrows(InvalidTaskTypeException.class, executable);
    }

    @Test
    public void getTaskFromInputMapShouldThrowInvalidContactMethodExceptionIfInvalidContactMethod() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BotService.TIME_AND_DATE_FORMAT);
        Date date = new Date(0);
        try {
            date = simpleDateFormat.parse("10:22:59 04-10-2024");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task TASK = new Task(
                String.valueOf(CHAT_ID), "name",
                date, TaskType.ONE,
                Method.SMS, "phoneNumber", "notificationText");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());
        botService.saveInput(CHAT_ID, InputState.TASK_TIME, "10:22:59 04-10-2024");
        botService.saveInput(CHAT_ID, InputState.TASK_TYPE, TASK.getTaskType().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, TASK.getContactMethod().toString() + " ");
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, TASK.getContactId());
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, TASK.getContactNotification());

        Executable executable = () -> {
            botService.getTaskFromInputMap(CHAT_ID);
        };

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void getTaskFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getTaskFromInputMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getTaskKeyFromInputMapShouldReturnFromMap() {
        String NAME = "NAME";
        botService.saveInput(CHAT_ID, InputState.TASK_NAME, NAME);

        Task task = new Task(String.valueOf(CHAT_ID), NAME);

        assertEquals(task, botService.getTaskKeyFromInputMap(CHAT_ID));
    }

    @Test
    public void getTaskKeyFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getTaskKeyFromInputMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void createTask() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BotService.TIME_AND_DATE_FORMAT);
        Date date = new Date(0);
        try {
            date = simpleDateFormat.parse("10:22:59 04-10-2024");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task TASK = new Task(
                String.valueOf(CHAT_ID), "name",
                date, TaskType.ONE,
                Method.SMS, "phoneNumber", "notificationText");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());
        botService.saveInput(CHAT_ID, InputState.TASK_TIME, "10:22:59 04-10-2024");
        botService.saveInput(CHAT_ID, InputState.TASK_TYPE, TASK.getTaskType().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, TASK.getContactMethod().toString());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, TASK.getContactId());
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, TASK.getContactNotification());

        botService.createTask(CHAT_ID);

        verify(botService).getTaskFromInputMap(CHAT_ID);
        verify(taskService).create(TASK);
        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void deleteTask() {
        Task TASK = new Task(String.valueOf(CHAT_ID), "name");

        botService.saveInput(CHAT_ID, InputState.TASK_NAME, TASK.getName());

        botService.deleteTask(CHAT_ID);

        verify(botService).getTaskKeyFromInputMap(CHAT_ID);
        verify(taskService).deleteByKey(Long.parseLong(TASK.getChatId()), TASK.getName());
        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void deleteAllTasks() {
        botService.deleteAllTasks(CHAT_ID);

        verify(taskService).deleteAllByChatId(CHAT_ID);
    }

    @Test
    public void showAllContacts() {
        botService.showAllContacts(CHAT_ID);

        verify(contacts).findAll(CHAT_ID);
    }

    @Test
    public void addContactShouldCallServiceAddContactWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Contact contact = new Contact(METHOD, STRING_2, STRING_3);

        botService.addContact(CHAT_ID);

        verify(contacts).addOne(CHAT_ID, contact);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void addContactShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.addContact(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void addContactShouldThrowIfInvalidMethod() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Executable executable = () -> botService.addContact(CHAT_ID);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void removeOneContactShouldCallServiceRemoveOneContactWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Contact contact = new Contact(METHOD, STRING_2, STRING_3);

        botService.removeOneContact(CHAT_ID);

        verify(contacts).removeOne(CHAT_ID, contact);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void removeOneContactShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.removeOneContact(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void removeOneContactShouldThrowIfInvalidMethod() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Executable executable = () -> botService.removeOneContact(CHAT_ID);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void removeManyContactShouldCallServiceRemoveManyContactWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Contact contact = new Contact(METHOD, STRING_2, STRING_3);

        botService.removeManyContacts(CHAT_ID);

        verify(contacts).removeMany(CHAT_ID, contact);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void removeManyContactShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.removeManyContacts(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void removeManyContactShouldThrowIfInvalidMethod() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Executable executable = () -> botService.removeManyContacts(CHAT_ID);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void showAllNotifications() {
        botService.showAllNotifications(CHAT_ID);

        verify(notifications).findAll(CHAT_ID);
    }

    @Test
    public void addNotificationShouldCallServiceAddNotificationWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_2);

        Notification notification = new Notification(STRING_1, STRING_2);

        botService.addNotification(CHAT_ID);

        verify(notifications).addOne(CHAT_ID, notification);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void addNotificationShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.addNotification(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void removeOneNotificationShouldCallServiceRemoveOneNotificationWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_2);

        Notification notification = new Notification(STRING_1, STRING_2);

        botService.removeOneNotification(CHAT_ID);

        verify(notifications).removeOne(CHAT_ID, notification);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void removeOneNotificationShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.addNotification(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void removeManyNotificationsShouldCallServiceRemoveManyNotificationsWithValuesFromInputMapAndClearMap() {
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_2);

        Notification notification = new Notification(STRING_1, STRING_2);

        botService.removeManyNotifications(CHAT_ID);

        verify(notifications).removeMany(CHAT_ID, notification);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void removeManyNotificationsShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.removeManyNotifications(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void setCustomPhraseFromInputMapShouldSetPhrase() {
        String customPhrase = "CUSTOM_PHRASE";

        botService.saveInput(CHAT_ID, InputState.CUSTOM_PHRASE, customPhrase);

        botService.setCustomPhraseFromInputMap(CHAT_ID);

        verify(botService).setUserCustomPhrase(CHAT_ID, customPhrase);

        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void setCustomPhraseFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.setCustomPhraseFromInputMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void cancelInputs() {
        botService.cancelInputs(CHAT_ID);

        verify(botService).setUserNextInputState(CHAT_ID, InputState.BASE);
        verify(botService).setUserNextInputGroup(CHAT_ID, InputGroup.BASE);
        verify(botService).clearUserInputs(CHAT_ID);
    }

    @Test
    public void setBaseInputAndGroupForAllUsers() {
        botService.setBaseInputAndGroupForAllUsers();

        verify(telegramUserService).setBaseInputAndGroupForAllUsers();
    }

    @Test
    public void setNextInputGroup() {
        botService.setUserNextInputGroup(CHAT_ID, IG);

        verify(telegramUserService).setInputGroup(CHAT_ID, IG);
    }

    @Test
    public void getNextInputGroup() {
        when(telegramUserService.getInputGroupOrBase(CHAT_ID)).thenReturn(IG);

        assertEquals(IG, botService.getUserNextInputGroupOrBase(CHAT_ID));
    }

    @Test
    public void setNextInputState() {
        botService.setUserNextInputState(CHAT_ID, IS);

        verify(telegramUserService).setInputState(CHAT_ID, IS);
    }

    @Test
    public void getNextInputState() {
        when(telegramUserService.getInputStateOrBase(CHAT_ID)).thenReturn(IS);

        assertEquals(IS, botService.getUserNextInputStateOrBase(CHAT_ID));
    }

    @Test
    public void setUserActionConfirmFlag() {
        botService.setUserActionConfirmFlag(CHAT_ID, true);

        verify(telegramUserService).setActionConfirmFlag(CHAT_ID, true);

        botService.setUserActionConfirmFlag(CHAT_ID, false);

        verify(telegramUserService).setActionConfirmFlag(CHAT_ID, false);
    }

    @Test
    public void getUserActionConfirmFlag() {
        when(telegramUserService.getActionConfirmFlag(CHAT_ID)).thenReturn(true);

        assertTrue(botService.getUserActionConfirmFlag(CHAT_ID));

        when(telegramUserService.getActionConfirmFlag(CHAT_ID)).thenReturn(false);

        assertFalse(botService.getUserActionConfirmFlag(CHAT_ID));
    }

    @Test
    public void setUserCustomPhrase() {
        botService.setUserCustomPhrase(CHAT_ID, "MOCK_STRING");

        verify(telegramUserService).setCustomPhrase(CHAT_ID, "MOCK_STRING");
    }

    @Test
    public void getUserCustomPhrase() {
        when(telegramUserService.getCustomPhrase(CHAT_ID)).thenReturn("MOCK_STRING");

        assertEquals("MOCK_STRING", botService.getUserCustomPhrase(CHAT_ID));
    }

    @Test
    public void saveInputSavesInputInMap() {
        Executable mapIsEmpty = () -> botService.getUserInputMapOrThrow(CHAT_ID);
        assertThrows(EmptyInputMapException.class, mapIsEmpty);

        botService.saveInput(CHAT_ID, IS, STRING_1);

        assertFalse(botService.getUserInputMapOrThrow(CHAT_ID).isEmpty());
    }

    @Test
    public void getUserInputReturnsInputsOfUser() {
        Executable mapIsEmpty = () -> botService.getUserInputMapOrThrow(CHAT_ID);
        assertThrows(EmptyInputMapException.class, mapIsEmpty);

        botService.saveInput(CHAT_ID, IS, STRING_1);
        botService.saveInput(ANOTHER_CHAT_ID, IS, STRING_1);

        Map<InputState, String> expected = Collections.singletonMap(IS, STRING_1);

        assertEquals(botService.getUserInputMapOrThrow(CHAT_ID), expected);
    }

    @Test
    public void clearUserInputsDeletesUserInputs() {
        botService.saveInput(CHAT_ID, IS, STRING_1);
        botService.saveInput(ANOTHER_CHAT_ID, IS, STRING_1);

        assertFalse(botService.getUserInputMapOrThrow(CHAT_ID).isEmpty());

        botService.clearUserInputs(CHAT_ID);

        Executable emptyMap = () -> botService.getUserInputMapOrThrow(CHAT_ID);
        assertThrows(EmptyInputMapException.class, emptyMap);
    }

    @Test
    public void getContactFromInputMapShouldReturnFromMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Contact contact = new Contact(METHOD, STRING_2, STRING_3);

        assertEquals(contact, botService.getContactFromInputsMap(CHAT_ID));
    }

    @Test
    public void getContactFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getContactFromInputsMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getContactFromInputMapShouldThrowIfInvalidMethod() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, STRING_1);
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        Executable executable = () -> botService.getContactFromInputsMap(CHAT_ID);

        assertThrows(InvalidContactMethodException.class, executable);
    }

    @Test
    public void getNotificationFromInputMapShouldReturnFromMap() {
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_2);

        Notification notification = new Notification(STRING_1, STRING_2);

        assertEquals(notification, botService.getNotificationFromInputsMap(CHAT_ID));
    }

    @Test
    public void getNotificationFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getNotificationFromInputsMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getUsernameAndPasswordFromInputMapShouldReturnFromMap() {
        botService.saveInput(CHAT_ID, InputState.USERNAME, STRING_1);
        botService.saveInput(CHAT_ID, InputState.PASSWORD, STRING_2);

        String[] usernamePassword = new String[2];
        usernamePassword[0] = STRING_1;
        usernamePassword[1] = STRING_2;

        assertArrayEquals(usernamePassword, botService.getUsernameAndPasswordFromInputsMap(CHAT_ID));
    }

    @Test
    public void getUsernameAndPasswordFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getUsernameAndPasswordFromInputsMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getSendOneRequestFromInputMapShouldReturnFromMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_3);

        SendOneRequest request = new SendOneRequest(METHOD.name(), STRING_2, STRING_3);

        assertEquals(request, botService.getSendOneRequestFromInputsMap(CHAT_ID));
    }

    @Test
    public void getSendOneRequestFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getSendOneRequestFromInputsMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getSendManyRequestFromInputMapShouldReturnFromMap() {
        botService.saveInput(CHAT_ID, InputState.CONTACT_METHOD, METHOD.name());
        botService.saveInput(CHAT_ID, InputState.CONTACT_ID, STRING_2);
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_NAME, STRING_3);

        SendManyRequest request = new SendManyRequest(METHOD.name(), STRING_2, STRING_3);

        assertEquals(request, botService.getSendManyRequestFromInputsMap(CHAT_ID));
    }

    @Test
    public void getSendManyRequestFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getSendManyRequestFromInputsMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getCustomPhraseFromInputMapShouldReturnFromMap() {
        String customPhrase = "CUSTOM_PHRASE";

        botService.saveInput(CHAT_ID, InputState.CUSTOM_PHRASE, customPhrase);

        String input = botService.getCustomPhraseFromInputMap(CHAT_ID);

        assertEquals(customPhrase, input);
    }

    @Test
    public void getCustomPhraseFromInputMapShouldThrowIfEmptyMap() {
        Executable executable = () -> botService.getCustomPhraseFromInputMap(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }

    @Test
    public void getUserInputMapOrThrowShouldReturnRightUser() {
        botService.saveInput(CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_1);
        botService.saveInput(ANOTHER_CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_1);

        Map<InputState, String> expected = Collections.singletonMap(InputState.NOTIFICATION_TEXT, STRING_1);

        assertEquals(expected, botService.getUserInputMapOrThrow(CHAT_ID));
    }

    @Test
    public void getUserInputMapOrThrowShouldThrowIfEmptyMap() {
        botService.saveInput(ANOTHER_CHAT_ID, InputState.NOTIFICATION_TEXT, STRING_1);

        Executable executable = () -> botService.getUserInputMapOrThrow(CHAT_ID);

        assertThrows(EmptyInputMapException.class, executable);
    }
}
