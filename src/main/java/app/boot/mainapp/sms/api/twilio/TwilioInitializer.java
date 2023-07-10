package app.boot.mainapp.sms.api.twilio;

import com.twilio.Twilio;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Component
public class TwilioInitializer {
    public TwilioInitializer(TwilioConfiguration twilioConfiguration) {
      Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
    }
}
