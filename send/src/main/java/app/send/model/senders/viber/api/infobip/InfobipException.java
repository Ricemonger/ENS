package app.send.model.senders.viber.api.infobip;

import app.utils.services.sender.exceptions.SenderApiException;

public class InfobipException extends SenderApiException {

    public InfobipException() {
        super();
    }

    public InfobipException(Throwable cause) {
        super(cause);
    }

    public InfobipException(String message) {
        super(message);
    }
}
