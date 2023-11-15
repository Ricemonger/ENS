package app.send.model.senders.amazonSes;

import app.send.model.senders.email.api.amazonSES.AmazonEmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class AmazonEmailSenderTests {

    private final static String SEND_TO = "leskotr24@gmail.com";

    @Autowired
    private AmazonEmailSender amazonEmailSender;

    @Test
    public void send() {
        amazonEmailSender.send(SEND_TO, "test notification one");
    }

    @Test
    public void bulkSend() {
        Map<String, List<String>> sendings = new HashMap<>();
        sendings.put("bulk test 1", Collections.singletonList(SEND_TO));
        sendings.put("bulk test 2", Collections.singletonList(SEND_TO));
        amazonEmailSender.bulkSend(sendings);
    }
}
