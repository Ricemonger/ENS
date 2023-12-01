package app.send.model.senders.infobip;

import app.send.model.senders.viber.api.infobip.InfobipViberSender;
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
public class InfobipViberSenderTests {

    @Value("${infobip.test.sendto}")
    public String SEND_TO;

    @Autowired
    private InfobipViberSender infobipViberSender;

    @Test
    public void send() {
        infobipViberSender.send(SEND_TO, "test notification one");
    }

    @Test
    public void bulkSend() {
        Map<String, List<String>> sendings = new HashMap<>();
        sendings.put("bulk test 1", Collections.singletonList(SEND_TO));
        sendings.put("bulk test 2", Collections.singletonList(SEND_TO));
        infobipViberSender.bulkSend(sendings);
    }
}
