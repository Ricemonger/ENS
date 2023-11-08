package app.send.service;


import app.send.service.senders.Sender;
import app.send.service.senders.email.EmailSender;
import app.send.service.senders.sms.SmsSender;
import app.send.service.senders.viber.ViberSender;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignService;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.notification.NotificationFeignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SendService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactFeignService contactFeignService;

    private final NotificationFeignService notificationFeignService;

    private final EmailSender emailSender;

    private final SmsSender smsSender;

    private final ViberSender viberSender;

    private final String DEFAULT_TEXT_PATTERN = "EMERGENCY NOTIFICATION MESSAGE BY %S!!";

    public void sendOne(String token, String username, String method, String contactId, String notificationText) {
        log.trace("sendOne method is executing with params: username-{}, method-{}, contactId-{}, notificationText-{}", username, method, contactId, notificationText);
        String notifText = String.format(DEFAULT_TEXT_PATTERN, username);
        if (notificationText != null && !notificationText.isBlank()) {
            notifText = notificationText;
        } else {
            try {
                String notificationName = contactFeignService.findOneByPrimaryKey(token, method, contactId).getNotificationName();
                notifText = notificationFeignService.findOneByPrimaryKey(token, notificationName).getText();
            } catch (NoSuchElementException e) {
            }
        }
        switch (method.toUpperCase()) {
            case "SMS":
                send(smsSender, username, contactId, notifText);
                break;
            case "EMAIL":
                send(emailSender, username, contactId, notifText);
                break;
            case "VIBER":
                send(viberSender, username, contactId, notifText);
                break;
            default:
                throw new IllegalArgumentException("WRONG METHOD NAME");
        }
    }

    public void sendAll(String token, String username) {
        log.trace("sendAll method is executing with params: username-{}", username);
        List<Contact> contacts = contactFeignService.findAllById(token);
        List<Contact> smsContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.SMS)).toList();
        List<Contact> emailContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.EMAIL)).toList();
        List<Contact> viberContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.VIBER)).toList();
        Map<String, String> notifications = notificationFeignService.getMapByAccountId(token);
        sendAllSms(username, smsContacts, notifications);
        sendAllEmails(username, emailContacts, notifications);
        sendAllViberMessages(username, viberContacts, notifications);
    }

    private void sendAllViberMessages(String username, List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(viberSender, username, contacts, notifications);
    }

    private void sendAllSms(String username, List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(smsSender, username, contacts, notifications);
    }

    private void sendAllEmails(String username, List<Contact> contacts, Map<String, String> notifications) {
        bulkSend(emailSender, username, contacts, notifications);
    }

    private void bulkSend(Sender sender, String username, List<Contact> contacts, Map<String, String> notifications) {
        sender.bulkSend(toSendMap(username, contacts, notifications));
        log.trace("bulk send notifications were sent with params: sender-{}, contacts-{}, notifications-{}", sender, contacts, notifications);
    }

    private void send(Sender sender, String username, String contactId, String notificationText) {
        sender.send(contactId, notificationText);
        log.trace("notification was sent with params: sender-{}, username-{}, contactId-{}, notificationText-{}", sender, username, contactId, notificationText);
    }

    private Map<String, String> toSendMap(String username, List<Contact> contacts, Map<String, String> notifications) {
        Map<String, String> sendMap = new HashMap<>();
        for (Contact contact : contacts) {
            String sendTo = contact.getContactId();
            String notificationText;
            try {
                notificationText = notifications.get(contact.getNotificationName());
            } catch (Exception e) {
                notificationText = String.format(DEFAULT_TEXT_PATTERN, username);
            }
            if (notificationText == null || notificationText.isBlank()) {
                notificationText = String.format(DEFAULT_TEXT_PATTERN, username);
            }
            sendMap.put(sendTo, notificationText);
        }
        return sendMap;
    }
}
