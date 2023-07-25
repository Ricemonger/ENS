package app.boot.sender.service.email.api.amazonSES;

import app.boot.sender.service.email.EmailSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AmazonEmailSender implements EmailSender {

    private final AmazonAuthConfiguration auth;

    private static final String SUBJECT = "EMERGENCY NOTIFICATION!";

    private static final Properties PROPERTIES = System.getProperties();

    static {
        PROPERTIES.put("mail.transport.protocol", "smtps");
        PROPERTIES.put("mail.smtp.port", 25);
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.starttls.required", "true");
    }

    public void send(String to, String text){
        Session session = Session.getDefaultInstance(PROPERTIES);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(auth.getSentFrom()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(SUBJECT);
            msg.setContent(text, "text/plain");
            Transport transport = session.getTransport();
            transport.connect(auth.getHost(), auth.getUsername(), auth.getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }catch (MessagingException e){
            throw new AmazonEmailException(e);
        }
    }
    @Override
    public void bulkSend(Map<String, String> sendings) {
        Session session;
        Transport transport;
        try {
            session = Session.getDefaultInstance(PROPERTIES);
            transport = session.getTransport();
            transport.connect(auth.getHost(), auth.getUsername(), auth.getPassword());
        } catch (MessagingException e) {
            throw new AmazonEmailException(e);
        }
        if(sendings.size()<50) {
            for (String sendTo : sendings.keySet()) {
                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(auth.getSentFrom()));
                    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
                    msg.setSubject(SUBJECT);
                    msg.setContent(sendings.get(sendTo), "text/plain");
                    transport.sendMessage(msg, msg.getAllRecipients());
                } catch (MessagingException e) {
                    throw new AmazonEmailException(e);
                }
            }
        }else{
            Map<String, List<String>> revertMap = new HashMap<>();
            for(Map.Entry<String,String> entry : sendings.entrySet()){
                String revertKey = entry.getValue();
                List<String> revertValue = revertMap.getOrDefault(revertKey, new ArrayList<String>());
                revertValue.add(entry.getKey());
                revertMap.put(revertKey,revertValue);
            }
            for(Map.Entry<String,List<String>> entry : revertMap.entrySet()){
                   String text = entry.getKey();
                   List<String> sendTo = entry.getValue();
                try {
                    InternetAddress[] addresses = (InternetAddress[])sendTo.stream().map(address -> {
                        try {
                            return new InternetAddress(address);
                        } catch (AddressException e) {
                            throw new AmazonEmailException(e);
                        }
                    }).toArray();
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(auth.getSentFrom()));
                    msg.setRecipients(Message.RecipientType.TO, addresses);
                    msg.setSubject(SUBJECT);
                    msg.setContent(text, "text/plain");
                    transport.sendMessage(msg, msg.getAllRecipients());
                } catch (MessagingException e) {
                    throw new AmazonEmailException(e);
                }
            }
        }
        try {
            transport.close();
        } catch (MessagingException e) {
            throw new AmazonEmailException(e);
        }
    }
}