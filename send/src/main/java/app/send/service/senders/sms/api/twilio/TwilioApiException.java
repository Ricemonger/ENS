package app.send.service.senders.sms.api.twilio;

import app.send.controller.exceptions.SenderApiException;

public class TwilioApiException extends SenderApiException {

    public TwilioApiException() {
        super();
    }

    public TwilioApiException(Throwable cause) {
        super(cause);
    }

    public TwilioApiException(String message) {
        super(message);
    }
}
