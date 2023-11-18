package app.send.model.senders.twilio;

import app.send.model.senders.sms.api.twilio.TwilioSmsSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TwilioSmsSenderTests {

    private final static String SEND_TO = "380953766409";

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
