package app.send.service.senders.sms.api.twilio;

import com.twilio.Twilio;
import org.springframework.stereotype.Component;
@Component
public class TwilioInitializer {
    public TwilioInitializer(TwilioAuthConfiguration twilioAuthConfiguration) {
      Twilio.init(twilioAuthConfiguration.getAccountSid(), twilioAuthConfiguration.getAuthToken());
    }
}
