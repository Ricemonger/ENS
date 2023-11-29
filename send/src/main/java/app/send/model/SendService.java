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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendService {

    public final static String DEFAULT_TEXT_PATTERN = "EMERGENCY NOTIFICATION MESSAGE!!";

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignClientService;

    private final EmailSender emailSender;

    private final SmsSender smsSender;

    private final ViberSender viberSender;

    public void sendOne(String token, String method, String contactId, String notificationText) {
        String notifText = getNotifTextFromDbIfEmptyOrDefaultIfEmptyInDb(token, method, contactId, notificationText);

        switch (getMethod(method)) {
            case SMS -> send(smsSender, contactId, notifText);
            case EMAIL -> send(emailSender, contactId, notifText);
            case VIBER -> send(viberSender, contactId, notifText);
        }
    }

    public void sendMany(String securityToken, String method, String contactId, String notificationName) {
        Method meth = getMethod(method);

        List<Contact> contacts =
                contactFeignClientService
                        .findAllById(securityToken)
                        .stream()
                        .filter(c -> (
                                c.getMethod().equals(meth)
                                        && c.getContactId().startsWith(contactId)
                                        && c.getNotificationName().startsWith(notificationName)))
                        .toList();

        Map<String, String> notifications = notificationFeignClientService.getMapByAccountId(securityToken);

        switch (meth) {
            case SMS -> sendManySmsMessages(contacts, notifications);
            case EMAIL -> sendManyEmailMessages(contacts, notifications);
            case VIBER -> sendManyViberMessages(contacts, notifications);
        }
    }

    public void sendAll(String token) {
        List<Contact> contacts = contactFeignClientService.findAllById(token);
        List<Contact> smsContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.SMS)).toList();
        List<Contact> emailContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.EMAIL)).toList();
        List<Contact> viberContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.VIBER)).toList();
        Map<String, String> notifications = notificationFeignClientService.getMapByAccountId(token);
        sendManySmsMessages(smsContacts, notifications);
        sendManyEmailMessages(emailContacts, notifications);
        sendManyViberMessages(viberContacts, notifications);
    }

    private String getNotifTextFromDbIfEmptyOrDefaultIfEmptyInDb(String token, String method, String contactId, String notificationText) {
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
        return notifText;
    }

    private Method getMethod(String method) {
        Method meth;
        try {
            meth = Method.valueOf(method.trim().toUpperCase(Locale.ROOT));
            return meth;
        } catch (NullPointerException | IllegalArgumentException e) {
            log.info("getMethod throws with method-{}, wrong method name entered", method);
            throw new IllegalArgumentException();
        }
    }

    private void sendManyViberMessages(List<Contact> contacts, Map<String, String> notifications) {
        log.debug("sendManyViberMessages called");
        bulkSend(viberSender, contacts, notifications);
        log.trace("sendManyViberMessages executed");
    }

    private void sendManySmsMessages(List<Contact> contacts, Map<String, String> notifications) {
        log.debug("sendManySmsMessages called");
        bulkSend(smsSender, contacts, notifications);
        log.trace("sendManySmsMessages executed");
    }

    private void sendManyEmailMessages(List<Contact> contacts, Map<String, String> notifications) {
        log.debug("sendManyEmailsMessages called");
        bulkSend(emailSender, contacts, notifications);
        log.trace("sendManyEmailsMessages executed");
    }

    private void bulkSend(Sender sender, List<Contact> contacts, Map<String, String> notifications) {
        log.debug("bulkSend called for sender-{}", sender);
        sender.bulkSend(toSendMap(contacts, notifications));
        log.trace("bulkSend executed for sender-{}", sender);
    }

    private void send(Sender sender, String contactId, String notificationText) {
        log.debug("send called for sender-{}, contactId-{}, notificationText-{}", sender, contactId, notificationText);
        sender.send(contactId, notificationText);
        log.trace("send executed for sender-{}, contactId-{}, notificationText-{}", sender, contactId, notificationText);
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
