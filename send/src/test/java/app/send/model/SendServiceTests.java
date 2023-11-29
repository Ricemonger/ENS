package app.send.model;

import app.send.model.senders.email.EmailSender;
import app.send.model.senders.sms.SmsSender;
import app.send.model.senders.viber.ViberSender;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClient;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClient;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
public class SendServiceTests {

    private final static String TOKEN = "TOKEN";

    private final static String CONTACT_ID = "CONTACT_ID";

    private final static String NOTIFICATION_TEXT = "NOTIFICATION_TEXT";

    private final static String NOTIFICATION_NAME = "NOTIFICATION_NAME";

    private final static String MOCK_STRING = "MOCK_STRING";

    private final static Map<String, String> MOCK_NOTIFICATION_MAP = Collections.singletonMap("MOCK", "MOCK");

    private final static List<Contact> MOCK_CONTACT_LIST = new ArrayList<>();

    static {
        MOCK_CONTACT_LIST.add(new Contact(Method.SMS, "MOCK", "MOCK"));
        MOCK_CONTACT_LIST.add(new Contact(Method.VIBER, "MOCK", "MOCK"));
        MOCK_CONTACT_LIST.add(new Contact(Method.EMAIL, "MOCK", "MOCK"));
    }

    @Spy
    private MockContactFeignClientService contactFeignClientService = new MockContactFeignClientService(null);

    @Spy
    private MockNotificationFeignClientService notificationFeignClientService =
            new MockNotificationFeignClientService(null);

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private SmsSender smsSender;

    @MockBean
    private ViberSender viberSender;

    private SendService sendService;

    @BeforeEach
    public void setUp() {
        sendService = new SendService(contactFeignClientService, notificationFeignClientService, emailSender, smsSender,
                viberSender);
    }

    @Test
    public void sendOneShouldUseEmailSenderIfEmailMethod() {
        sendService.sendOne(TOKEN, Method.EMAIL.name(), CONTACT_ID, NOTIFICATION_TEXT);

        verifyNoInteractions(contactFeignClientService, notificationFeignClientService);

        verify(emailSender).send(CONTACT_ID, NOTIFICATION_TEXT);
    }

    @Test
    public void sendOneShouldUseSmsSenderIfSmsMethod() {
        sendService.sendOne(TOKEN, Method.SMS.name(), CONTACT_ID, NOTIFICATION_TEXT);

        verifyNoInteractions(contactFeignClientService, notificationFeignClientService);

        verify(smsSender).send(CONTACT_ID, NOTIFICATION_TEXT);
    }

    @Test
    public void sendOneShouldUseViberSenderIfViberMethod() {
        sendService.sendOne(TOKEN, Method.VIBER.name(), CONTACT_ID, NOTIFICATION_TEXT);

        verifyNoInteractions(contactFeignClientService, notificationFeignClientService);

        verify(viberSender).send(CONTACT_ID, NOTIFICATION_TEXT);
    }

    @Test
    public void sendOneShouldUseFeignClientToFindNotificationTextIfNotProvided() {
        sendService.sendOne(TOKEN, Method.VIBER.name(), CONTACT_ID, "");

        verify(contactFeignClientService).findOneByPrimaryKey(TOKEN, Method.VIBER.name(), CONTACT_ID);
        verify(notificationFeignClientService).findOneByPrimaryKey(TOKEN, MOCK_STRING);

        verify(viberSender).send(CONTACT_ID, MOCK_STRING);
    }

    @Test
    public void sendManyShouldUseSmsSenderIfSmsMethod() {
        sendService.sendMany(TOKEN, Method.SMS.name(), CONTACT_ID, NOTIFICATION_NAME);

        verify(smsSender).bulkSend(anyMap());
        verifyNoInteractions(viberSender);
        verifyNoInteractions(emailSender);
    }

    @Test
    public void sendManyShouldUseViberSenderIfViberMethod() {
        sendService.sendMany(TOKEN, Method.VIBER.name(), CONTACT_ID, NOTIFICATION_NAME);

        verify(viberSender).bulkSend(anyMap());
        verifyNoInteractions(smsSender);
        verifyNoInteractions(emailSender);
    }

    @Test
    public void sendManyShouldUseEmailSenderIfEmailMethod() {
        sendService.sendMany(TOKEN, Method.EMAIL.name(), CONTACT_ID, NOTIFICATION_NAME);

        verify(emailSender).bulkSend(anyMap());
        verifyNoInteractions(viberSender);
        verifyNoInteractions(smsSender);
    }

    @Test
    public void sendManyShouldFindAllByIdAndFilter() {
        Contact mock = MOCK_CONTACT_LIST.get(0);
        sendService.sendMany(TOKEN, mock.getMethod().name(), mock.getContactId(), mock.getNotificationName());

        verify(contactFeignClientService).findAllById(TOKEN);
    }

    @Test
    public void sendAllShouldCallAllSenders() {
        sendService.sendAll(TOKEN);

        verify(contactFeignClientService).findAllById(TOKEN);
        verify(notificationFeignClientService).getMapByAccountId(TOKEN);

        verify(emailSender).bulkSend(anyMap());
        verify(smsSender).bulkSend(anyMap());
        verify(viberSender).bulkSend(anyMap());
    }

    private static class MockContactFeignClientService extends ContactFeignClientService {

        public MockContactFeignClientService(ContactFeignClient contactFeignClient) {
            super(contactFeignClient);
        }

        @Override
        public Contact findOneByPrimaryKey(String token, String method, String contactId) {
            return new Contact(Method.valueOf(method), contactId, MOCK_STRING);
        }

        @Override
        public List<Contact> findAllById(String token) {
            return MOCK_CONTACT_LIST;
        }
    }

    private static class MockNotificationFeignClientService extends NotificationFeignClientService {

        public MockNotificationFeignClientService(NotificationFeignClient notificationFeignClient) {
            super(notificationFeignClient);
        }

        @Override
        public Notification findOneByPrimaryKey(String token, String notificationName) {
            return new Notification(notificationName, MOCK_STRING);
        }

        @Override
        public Map<String, String> getMapByAccountId(String token) {
            return MOCK_NOTIFICATION_MAP;
        }
    }
}
