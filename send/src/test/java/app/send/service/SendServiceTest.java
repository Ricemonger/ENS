package app.send.service;

import app.send.service.contact.ContactService;
import app.send.service.contact.Method;
import app.send.service.notification.NotificationService;
import app.send.service.senders.email.EmailSender;
import app.send.service.senders.sms.SmsSender;
import app.send.service.senders.viber.ViberSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SendServiceTest {

    @Mock
    private ContactService contactService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EmailSender emailSender;
    @Mock
    private SmsSender smsSender;
    @Mock
    private ViberSender viberSender;

    private SendService sendService;

    @BeforeEach
    void setUp() {
        sendService = new SendService(contactService, notificationService, emailSender, smsSender, viberSender);
    }

    @Test
    void sendOneEmail() {
        String id = "contactId";
        String text = "text";
        sendService.sendOne("token", "username", Method.EMAIL.name(), id, text);
        verify(emailSender).send(id, text);
        verifyNoInteractions(smsSender, viberSender);
    }

    @Test
    void sendOneSMS() {
        String id = "contactId";
        String text = "text";
        sendService.sendOne("token", "username", Method.SMS.name(), id, text);
        verify(smsSender).send(id, text);
        verifyNoInteractions(emailSender, viberSender);
    }

    @Test
    void sendOneViber() {
        String id = "contactId";
        String text = "text";
        sendService.sendOne("token", "username", Method.VIBER.name(), id, text);
        verify(viberSender).send(id, text);
        verifyNoInteractions(emailSender, smsSender);
    }

    @Test
    void sendWrongMethod() {
        String id = "contactId";
        String text = "text";
        Executable executable = () -> sendService.sendOne("token", "username", "wrongMethodName", id, text);
        assertThrows(IllegalArgumentException.class, executable);
        verifyNoInteractions(emailSender, smsSender, viberSender);
    }

    @Test
    void sendAll() {
        sendService.sendAll("token", "username");
        verify(smsSender).bulkSend(anyMap());
        verify(emailSender).bulkSend(anyMap());
        verify(viberSender).bulkSend(anyMap());
    }
}