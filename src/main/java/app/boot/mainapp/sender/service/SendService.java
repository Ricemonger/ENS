package app.boot.mainapp.sender.service;

import app.boot.mainapp.contact.model.Contact;
import app.boot.mainapp.contact.model.Method;
import app.boot.mainapp.contact.service.ContactService;
import app.boot.mainapp.notification.service.NotificationService;
import app.boot.mainapp.sender.service.email.service.EmailSender;
import app.boot.mainapp.sender.service.sms.service.SmsSender;
import app.boot.mainapp.sender.service.viber.ViberSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendService {
    private final ContactService contactService;
    private final NotificationService notificationService;
    private final EmailSender emailSender;
    private final SmsSender smsSender;
    private final ViberSender viberSender;

    public void sendOne(String username,String method, String contactId) {
        String notificationName = contactService.findOneByPrimaryKey(username,method,contactId).getNotificationName();
        String notificationText = notificationService.findOneByPrimaryKey(username,notificationName).getText();
        switch (method.toUpperCase()){
            case "SMS":
                send(smsSender,username,contactId,notificationText);
                break;
            case "EMAIL":
                send(emailSender,username,contactId,notificationText);
                break;
            case "VIBER":
                send(viberSender,username,contactId,notificationText);
            default:
                throw new IllegalArgumentException("WRONG METHOD NAME");
        }
    }
    public void sendAll(String username) {
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
            notificationText = String.format("EMERGENCY NOTIFICATION BY %s!",username);
        }
        sender.send(contactId,notificationText);
    }
}
