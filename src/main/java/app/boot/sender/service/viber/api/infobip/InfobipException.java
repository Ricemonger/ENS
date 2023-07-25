package app.boot.sender.service.viber.api.infobip;

import app.boot.sender.controller.exceptions.SenderApiException;

public class InfobipException extends SenderApiException {

    public InfobipException(){
        super();
    }

    public InfobipException(Throwable cause){
        super(cause);
    }

    public InfobipException(String message) {
        super(message);
    }
}
