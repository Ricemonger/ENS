package app.boot.sender.service;

import app.boot.contact.model.Contact;
import app.boot.contact.model.Method;
import app.boot.contact.service.ContactService;
import app.boot.notification.service.NotificationService;
import app.boot.sender.service.email.service.EmailSender;
import app.boot.sender.service.sms.service.SmsSender;
import app.boot.sender.service.viber.ViberSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public void sendOne(String username,String method, String contactId, String notificationText) {
        log.trace("sendOne method is executing with params: username-{}, method-{}, contactId-{}, notificationText-{}",username,method,contactId,notificationText);
        String notifText = String.format(DEFAULT_TEXT_PATTERN, username);
        if(notificationText!=null) {
            notifText = notificationText;
        }
        else{
            try {
                String notificationName = contactService.findOneByPrimaryKey(username, method, contactId).getNotificationName();
                notifText = notificationService.findOneByPrimaryKey(username, notificationName).getText();
            } catch (NoSuchElementException e) {
            }
        }
        switch (method.toUpperCase()){
            case "SMS":
                send(smsSender,username,contactId,notifText);
                break;
            case "EMAIL":
                send(emailSender,username,contactId,notifText);
                break;
            case "VIBER":
                send(viberSender,username,contactId,notifText);
            default:
                throw new IllegalArgumentException("WRONG METHOD NAME");
        }
    }
    public void sendAll(String username) {
        log.trace("sendAll method is executing with params: username-{}",username);
        List<Contact> contacts = contactService.findAllByUsername(username);
        List<Contact> smsContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.SMS)).toList();
        List<Contact> emailContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.EMAIL)).toList();
        List<Contact> viberContacts = contacts.stream().filter(contact -> contact.getMethod().equals(Method.VIBER)).toList();
        Map<String, String> notificationMap = notificationService.getMap(username);
        sendAllSms(username,smsContacts,notificationMap);
        sendAllEmails(username,emailContacts,notificationMap);
        sendAllViberMessages(username,viberContacts,notificationMap);
    }
    private void sendAllViberMessages(String username, List<Contact> contacts, Map<String, String> notificationMap) {
        for (Contact contact : contacts){
            send(viberSender,username,contact.getContactId(),notificationMap.get(contact.getNotificationName()));
        }
    }
    private void sendAllSms(String username, List<Contact> contacts, Map<String, String> notificationMap){
        for (Contact contact : contacts){
            send(smsSender,username,contact.getContactId(),notificationMap.get(contact.getNotificationName()));
        }
    }
    private void sendAllEmails(String username, List<Contact> contacts, Map<String, String> notificationMap){
        for (Contact contact : contacts){
            send(emailSender,username,contact.getContactId(),notificationMap.get(contact.getNotificationName()));
        }
    }
    private void send(Sender sender, String username, String contactId, String notificationText){
        if(notificationText==null || notificationText.isBlank()){
            notificationText = String.format(DEFAULT_TEXT_PATTERN,username);
        }
        sender.sendLogged(contactId,notificationText);
        log.trace("notification was sent with params: sender-{}, username-{}, contactId-{}, notificationText-{}",sender,username,contactId,notificationText);
    }
}
