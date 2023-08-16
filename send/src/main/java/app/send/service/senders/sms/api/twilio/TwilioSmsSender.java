package app.send.service.senders.sms.api.twilio;

import app.send.service.senders.sms.SmsSender;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TwilioSmsSender implements SmsSender {

    private final TwilioAuthConfiguration twilioAuthConfiguration;

    @Override
    public void send(String sendTo, String text){
        try {
            String to = sendTo;
            if (!to.startsWith("+")) {
                to = "+" + to;
            }
            PhoneNumber toNumber = new PhoneNumber(to);
            PhoneNumber fromNumber = new PhoneNumber(twilioAuthConfiguration.getTrialNumber());
            MessageCreator creator = Message.creator(toNumber, fromNumber, text);
            creator.create();
        }catch (ApiException e){
            throw new TwilioApiException(e.getMessage());
        }
    }

    @Override
    public void bulkSend(Map<String, String> sendings) {
        PhoneNumber from = new PhoneNumber(twilioAuthConfiguration.getTrialNumber());
        try {
            sendings.entrySet().stream().parallel().forEach(entry->{
                String to = entry.getKey();
                if(!to.startsWith("+")){
                    to = "+" + to;
                }
                Message.creator(new PhoneNumber(to),from,entry.getValue()).create();
            }
            );
        }catch (ApiException e){
            throw new TwilioApiException(e);
        }
    }
}
