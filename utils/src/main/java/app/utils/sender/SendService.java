package app.utils.sender;

import app.utils.contact.Contact;
import app.utils.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendService {

    private final SendClient sendClient;

    private final NotificationService notificationService;

    public void sendAll(String securityToken) {
        sendClient.sendAll(securityToken);
        log.trace("sendAll method was executed with securityToken:{}", securityToken);
    }

    public void sendOne(String securityToken, Contact contact) {
        String method = contact.getMethod().name();
        String contactId = contact.getContactId();

        String notificationName = contact.getNotificationName();
        String notificationText = notificationService.findOneByPrimaryKey(securityToken, notificationName).getText();

        SendOneRequest request = new SendOneRequest(method, contactId, notificationText);

        sendClient.sendOne(securityToken, request);
        log.trace("sendOne method was executed with securityToken-{}, request-{}", securityToken, request);
    }
}
