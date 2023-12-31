package app.send.model.senders.email.api.amazonSES;

import app.utils.services.sender.exceptions.SenderApiException;

public class AmazonEmailException extends SenderApiException {

    public AmazonEmailException(Throwable cause) {
        super(cause);
    }

    public AmazonEmailException() {
        super();
    }

    public AmazonEmailException(String message) {
        super(message);
    }
}
