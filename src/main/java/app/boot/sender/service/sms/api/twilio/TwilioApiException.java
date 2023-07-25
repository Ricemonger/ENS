package app.boot.sender.service.sms.api.twilio;

import app.boot.sender.controller.exceptions.SenderApiException;

public class TwilioApiException extends SenderApiException {

    public TwilioApiException(){
        super();
    }

    public TwilioApiException(Throwable cause) {
        super(cause);
    }
    public TwilioApiException(String message) {
        super(message);
    }
}
