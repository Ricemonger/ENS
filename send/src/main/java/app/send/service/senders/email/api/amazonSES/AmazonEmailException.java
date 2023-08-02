package app.send.service.senders.email.api.amazonSES;

import app.send.controller.exceptions.SenderApiException;

public class AmazonEmailException extends SenderApiException {

    public AmazonEmailException(Throwable cause){
        super(cause);
    }
    public AmazonEmailException(){
        super();
    }

    public AmazonEmailException(String message) {
        super(message);
    }
}
