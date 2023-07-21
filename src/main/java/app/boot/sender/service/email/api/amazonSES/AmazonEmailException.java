package app.boot.sender.service.email.api.amazonSES;

import app.boot.sender.controller.exceptions.SenderApiException;

public class AmazonEmailException extends SenderApiException {

    public AmazonEmailException(){
        super();
    }

    public AmazonEmailException(String message) {
        super(message);
    }
}
