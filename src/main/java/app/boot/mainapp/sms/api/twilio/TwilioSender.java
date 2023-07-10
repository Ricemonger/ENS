package app.boot.mainapp.sms.api.twilio;

import app.boot.mainapp.sms.dto.SmsRequest;
import app.boot.mainapp.sms.service.SmsSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSender implements SmsSender {

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSMS(SmsRequest smsRequest) {
        String toNumber = smsRequest.phoneNumber();
        if(!toNumber.startsWith("+")){
            toNumber = "+" + toNumber;
        }
        PhoneNumber to = new PhoneNumber(toNumber);
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
        String message = smsRequest.message();
        MessageCreator creator = Message.creator(to, from, message);
        creator.create();
    }
}
