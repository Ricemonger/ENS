package app.boot.mainapp.sender.email.api.amazonSES;

import app.boot.mainapp.sender.email.service.EmailSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
@PropertySource("authentication.properties")
public class AmazonEmailSender implements EmailSender {
    @Value("${amazon.email.sender}")
    private String sentFrom;
    private final String SUBJECT = "EMERGENCY NOTIFICATION!";
    @Value("${amazon.smtp.host}")
    private String host;
    @Value("${amazon.smtp.username}")
    private String username;
    @Value("${amazon.smtp.password}")
    private String password;
    public void send(String to, String text){
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(sentFrom));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(SUBJECT);
            msg.setContent(text, "text/plain");
            Transport transport = session.getTransport();
            transport.connect(host,username,password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            System.out.println("ERROR: " + e.toString());
        }
    }
}