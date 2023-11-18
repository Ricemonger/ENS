package app.send.model.senders.email.api.amazonSES;

import app.send.model.senders.email.EmailSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;


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

    @Override
    public void send(String sendTo, String text) {
        Session session = createSession();
        sendToOneEmail(sendTo, text, session);
    }

    @Override
    public void bulkSend(Map<String, List<String>> sendings) {
        Session session = createSession();

        for (Map.Entry<String, List<String>> entry : sendings.entrySet()) {
            String text = entry.getKey();
            List<String> sendToList = entry.getValue();
            sendToManyEmails(sendToList, text, session);
        }
    }

    private Session createSession() {
        return Session.getDefaultInstance(PROPERTIES);
    }

    private void sendToOneEmail(String to, String text, Session session) {
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(auth.getSentFrom()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(SUBJECT);
            msg.setContent(text, "text/plain");
            Transport transport = session.getTransport();
            transport.connect(auth.getHost(), auth.getUsername(), auth.getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
            closeTransport(transport);
        } catch (MessagingException e) {
            throw new AmazonEmailException(e);
        }
    }

    private void sendToManyEmails(List<String> sendToList, String text, Session session) {
        try {
            Transport transport = session.getTransport();
            transport.connect(auth.getHost(), auth.getUsername(), auth.getPassword());
            InternetAddress[] addresses = sendToList.stream().map(address -> {
                try {
                    return new InternetAddress(address);
                } catch (AddressException e) {
                    throw new AmazonEmailException(e);
                }
            }).toArray(InternetAddress[]::new);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(auth.getSentFrom()));
            msg.setRecipients(Message.RecipientType.TO, addresses);
            msg.setSubject(SUBJECT);
            msg.setContent(text, "text/plain");
            transport.sendMessage(msg, msg.getAllRecipients());
            closeTransport(transport);
        } catch (MessagingException e) {
            throw new AmazonEmailException(e);
        }
    }

    private void closeTransport(Transport transport) {
        try {
            transport.close();
        } catch (MessagingException e) {
            throw new AmazonEmailException();
        }
    }
}