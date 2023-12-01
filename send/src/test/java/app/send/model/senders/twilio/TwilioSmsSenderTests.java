package app.send.model.senders.twilio;

import app.send.model.senders.sms.api.twilio.TwilioSmsSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestPropertySource("classpath:authentication.properties")
public class TwilioSmsSenderTests {

    @Value("${twilio.test.sendto}")
    public String SEND_TO;

    @Autowired
    private TwilioSmsSender twilioSmsSender;

    @Test
    public void send() {
        twilioSmsSender.send(SEND_TO, "test notification one");
    }

    @Test
    public void bulkSend() {
        Map<String, List<String>> sendings = new HashMap<>();
        sendings.put("bulk test 1", Collections.singletonList(SEND_TO));
        sendings.put("bulk test 2", Collections.singletonList(SEND_TO));
        twilioSmsSender.bulkSend(sendings);
    }
}
