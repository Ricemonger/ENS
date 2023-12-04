package app.send;

import app.send.controller.SendController;
import app.send.controller.SendControllerService;
import app.send.model.SendService;
import app.send.model.senders.email.EmailSender;
import app.send.model.senders.email.api.amazonSES.AmazonAuthConfiguration;
import app.send.model.senders.sms.SmsSender;
import app.send.model.senders.sms.api.twilio.TwilioAuthConfiguration;
import app.send.model.senders.sms.api.twilio.TwilioInitializer;
import app.send.model.senders.viber.ViberSender;
import app.send.model.senders.viber.api.infobip.InfobipAuthConfigurer;
import app.utils.logger.AroundLogger;
import app.utils.services.contact.feign.ContactFeignClient;
import app.utils.services.contact.feign.ContactFeignClientService;
import app.utils.services.notification.feign.NotificationFeignClient;
import app.utils.services.notification.feign.NotificationFeignClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SendServerTests {

    @Autowired
    private AroundLogger aroundLogger;

    @Autowired
    private SendController sendController;

    @Autowired
    private SendControllerService sendControllerService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private ViberSender viberSender;

    @Autowired
    private SendService sendService;

    @Autowired
    private AmazonAuthConfiguration amazonAuthConfiguration;

    @Autowired
    private TwilioAuthConfiguration twilioAuthConfiguration;

    @Autowired
    private TwilioInitializer twilioInitializer;

    @Autowired
    private InfobipAuthConfigurer infobipAuthConfigurer;

    @Autowired
    private ContactFeignClientService contactFeignClientService;

    @Autowired
    private ContactFeignClient contactFeignClient;

    @Autowired
    private NotificationFeignClientService notificationFeignClientService;

    @Autowired
    private NotificationFeignClient notificationFeignClient;

    @Test
    public void contextLoads() {
        assertNotNull(aroundLogger);
        assertNotNull(sendController);
        assertNotNull(sendControllerService);
        assertNotNull(emailSender);
        assertNotNull(smsSender);
        assertNotNull(viberSender);
        assertNotNull(sendService);
        assertNotNull(amazonAuthConfiguration);
        assertNotNull(twilioAuthConfiguration);
        assertNotNull(twilioInitializer);
        assertNotNull(infobipAuthConfigurer);
        assertNotNull(contactFeignClient);
        assertNotNull(contactFeignClientService);
        assertNotNull(notificationFeignClient);
        assertNotNull(notificationFeignClientService);
    }
}
