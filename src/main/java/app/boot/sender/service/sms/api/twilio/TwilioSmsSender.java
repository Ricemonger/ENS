package app.boot.sender.service.sms.api.twilio;

import app.boot.sender.service.sms.service.SmsSender;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender extends SmsSender {

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void send(String sendTo, String text){
        try {
            String to = sendTo;
            if (!to.startsWith("+")) {
                to = "+" + to;
            }
            PhoneNumber toNumber = new PhoneNumber(to);
            PhoneNumber fromNumber = new PhoneNumber(twilioConfiguration.getTrialNumber());
            MessageCreator creator = Message.creator(toNumber, fromNumber, text);
            creator.create();
        }catch (ApiException e){
            throw new TwilioApiException(e.getMessage());
        }
    }
}
