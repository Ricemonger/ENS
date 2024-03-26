package app.send.model.senders.sms.api.twilio;

import app.utils.services.sender.exceptions.SenderApiException;

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
