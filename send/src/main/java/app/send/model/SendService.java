package app.send.model;


import app.send.model.senders.Sender;
import app.send.model.senders.email.EmailSender;
import app.send.model.senders.sms.SmsSender;
import app.send.model.senders.viber.ViberSender;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SendService {

    public final static String DEFAULT_TEXT_PATTERN = "EMERGENCY NOTIFICATION MESSAGE!!";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignClientService;

    private final EmailSender emailSender;

    private final SmsSender smsSender;

    private final ViberSender viberSender;

    public void sendOne(String token, String method, String contactId, String notificationText) {
        log.trace("sendOne method is executing with params: token-{}, method-{}, contactId-{}, notificationText-{}",
                token, method, contactId, notificationText);
        String notifText = DEFAULT_TEXT_PATTERN;
        if (notificationText != null && !notificationText.isBlank()) {
            notifText = notificationText;
        } else {
            try {
                String notificationName = contactFeignClientService.findOneByPrimaryKey(token, method, contactId).getNotificationName();
                notifText = notificationFeignClientService.findOneByPrimaryKey(token, notificationName).getText();
            } catch (NoSuchElementException e) {
            }
        }
        switch (method.toUpperCase()) {
            case "SMS":
                send(smsSender, contactId, notifText);
                break;
            case "EMAIL":
                send(emailSender, contactId, notifText);
                break;
            case "VIBER":
                send(viberSender, contactId, notifText);
                break;
            default:
                throw new IllegalArgumentException("WRONG METHOD NAME");
        }
    }

    public void sendAll(String token) {
        log.trace("sendAll method is executing with param: token-{}", token);
        List<Contact> contacts = contactFeignClientService.findAllById(token);
        List<Contact> smsContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.SMS)).toList();
        List<Contact> emailContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.EMAIL)).toList();
        List<Contact> viberContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.VIBER)).toList();
        Map<String, String> notifications = notificationFeignClientService.getMapByAccountId(token);
        sendAllSms(smsContacts, notifications);
        sendAllEmails(emailContacts, notifications);
        sendAllViberMessages(viberContacts, notifications);
    }

    private void sendAllViberMessages(List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(viberSender, contacts, notifications);
    }

    private void sendAllSms(List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(smsSender, contacts, notifications);
    }

    private void sendAllEmails(List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(emailSender, contacts, notifications);
    }

    private void bulkSend(Sender sender, List<Contact> contacts, Map<String, String> notifications) {
        sender.bulkSend(toSendMap(contacts, notifications));
        log.trace("bulk send notifications were sent with params: sender-{}, contacts-{}, notifications-{}", sender, contacts, notifications);
    }

    private void send(Sender sender, String contactId, String notificationText) {
        sender.send(contactId, notificationText);
        log.trace("notification was sent with params: sender-{}, contactId-{}, notificationText-{}", sender, contactId, notificationText);
    }

    private Map<String, List<String>> toSendMap(List<Contact> contacts, Map<String, String> notifications) {
        Map<String, List<String>> sendMap = new HashMap<>();

        for (Contact contact : contacts) {
            String sendTo = contact.getContactId();
            String notificationText;
            try {
                notificationText = notifications.get(contact.getNotificationName());
            } catch (Exception e) {
                notificationText = DEFAULT_TEXT_PATTERN;
            }
            if (notificationText == null || notificationText.isBlank()) {
                notificationText = DEFAULT_TEXT_PATTERN;
            }
            List<String> sendToList = sendMap.get(notificationText);
            if (sendToList == null) {
                sendToList = new ArrayList<>();
            }
            sendToList.add(sendTo);
            sendMap.put(notificationText, sendToList);
        }

        return sendMap;
    }
}
