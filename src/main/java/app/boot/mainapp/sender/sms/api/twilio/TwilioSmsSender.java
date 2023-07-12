package app.boot.mainapp.sender.sms.api.twilio;

import app.boot.mainapp.sender.sms.service.SmsSender;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender implements SmsSender {

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void send(String sendTo, String text) throws ApiException {
        String to = sendTo;
        if(!to.startsWith("+")){
            to = "+" + to;
        }
        PhoneNumber toNumber = new PhoneNumber(to);
        PhoneNumber fromNumber = new PhoneNumber(twilioConfiguration.getTrialNumber());
        MessageCreator creator = Message.creator(toNumber, fromNumber, text);
        creator.create();
    }
}
