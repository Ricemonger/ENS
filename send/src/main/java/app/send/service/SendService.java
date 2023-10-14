package app.send.service;

import app.send.service.contact.Contact;
import app.send.service.contact.ContactService;
import app.send.service.contact.Method;
import app.send.service.notification.NotificationService;
import app.send.service.senders.Sender;
import app.send.service.senders.email.EmailSender;
import app.send.service.senders.sms.SmsSender;
import app.send.service.senders.viber.ViberSender;
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
    private final ContactService contactService;
    private final NotificationService notificationService;
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
                String notificationName = contactService.findOneByPrimaryKey(token, method, contactId).getNotificationName();
                notifText = notificationService.findOneByPrimaryKey(token, notificationName).getText();
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
        List<Contact> contacts = contactService.findAllByUsername(token);
        List<Contact> smsContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.SMS)).toList();
        List<Contact> emailContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.EMAIL)).toList();
        List<Contact> viberContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.VIBER)).toList();
        Map<String, String> notifications = notificationService.getMap(token);
        sendAllSms(username, smsContacts, notifications);
        sendAllEmails(username, emailContacts, notifications);
        sendAllViberMessages(username, viberContacts, notifications);
    }

    private void sendAllViberMessages(String username, List<Contact> contacts, Map<String, String> notifications) {
        viberSender.bulkSend(toSendMap(username, contacts, notifications));
    }

    private void sendAllSms(String username, List<Contact> contacts, Map<String, String> notifications) {
        smsSender.bulkSend(toSendMap(username, contacts, notifications));
    }

    private void sendAllEmails(String username, List<Contact> contacts, Map<String, String> notifications) {
        emailSender.bulkSend(toSendMap(username, contacts, notifications));
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

    private void send(Sender sender, String username, String contactId, String notificationText) {
        sender.send(contactId, notificationText);
        log.trace("notification was sent with params: sender-{}, username-{}, contactId-{}, notificationText-{}", sender, username, contactId, notificationText);
    }
}
