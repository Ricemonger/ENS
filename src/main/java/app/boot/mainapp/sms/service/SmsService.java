package app.boot.mainapp.sms.service;

import app.boot.authentication.security.JwtUtil;
import app.boot.mainapp.contact.model.Contact;
import app.boot.mainapp.contact.model.Method;
import app.boot.mainapp.contact.service.ContactService;
import app.boot.mainapp.notification.model.Notification;
import app.boot.mainapp.notification.service.NotificationService;
import app.boot.mainapp.sms.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("application.properties")
public class SmsService {
    private final SmsSender smsSender;

    private final NotificationService notificationService;

    private final ContactService contactsService;

    private final JwtUtil jwtUtil;

    public void sendSMS(SmsRequest smsRequest){
        smsSender.sendSMS(smsRequest);
    }

    public void sendOne(String token, String phoneNumber, String notificationName) {
        Notification notification = notificationService.findOneByPrimaryKey(jwtUtil.extractUsername(token),notificationName);
        SmsRequest request = new SmsRequest(phoneNumber,notification.getText());
        sendSMS(request);
    }

    public void sendAll(String token) {
        String username = jwtUtil.extractUsername(token);
        List<Contact> contactList = contactsService
                .findAllByUsername(username)
                .stream()
                .filter(contact -> contact.getMethod().equals(Method.SMS))
                .toList();
        Map<String, String> notificationMap = notificationService
                .findAllByUsername(username)
                .stream()
                .collect(Collectors.toMap(Notification::getName,Notification::getText));
        for(Contact contact : contactList){
            SmsRequest request = new SmsRequest(contact.getContactId(),notificationMap.get(contact.getNotificationName()));
            sendSMS(request);
        }
    }
}
