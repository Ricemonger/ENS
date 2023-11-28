package app.utils.feign_clients.sender;

import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import app.utils.feign_clients.sender.dto.SendOneRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendFeignClientService {

    private final SendFeignClient sendFeignClient;

    private final NotificationFeignClientService notificationFeignClientService;

    public void sendAll(String securityToken) {
        sendFeignClient.sendAll(securityToken);
        log.trace("sendAll method was executed with securityToken:{}", securityToken);
    }

    public void sendOne(String securityToken, Contact contact) {
        String method = contact.getMethod().name();
        String contactId = contact.getContactId();

        String notificationName = contact.getNotificationName();
        String notificationText = notificationFeignClientService.findOneByPrimaryKey(securityToken, notificationName).getText();

        SendOneRequest request = new SendOneRequest(method, contactId, notificationText);

        sendFeignClient.sendOne(securityToken, request);
        log.trace("sendOne method was executed with securityToken-{}, request-{}", securityToken, request);
    }
}
