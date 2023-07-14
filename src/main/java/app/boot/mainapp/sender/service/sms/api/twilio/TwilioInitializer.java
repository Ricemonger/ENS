package app.boot.mainapp.sender.service.sms.api.twilio;

import com.twilio.Twilio;
import org.springframework.stereotype.Component;
@Component
public class TwilioInitializer {
    public TwilioInitializer(TwilioConfiguration twilioConfiguration) {
      Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
    }
}
