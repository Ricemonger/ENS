package app.boot.sender.service.viber.api.infobip;

import app.boot.sender.SenderApiException;

public class InfobipException extends SenderApiException {

    public InfobipException(){
        super();
    }

    public InfobipException(String message) {
        super(message);
    }
}
