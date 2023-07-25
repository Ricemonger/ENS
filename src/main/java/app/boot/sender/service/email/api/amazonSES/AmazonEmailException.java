package app.boot.sender.service.email.api.amazonSES;

import app.boot.sender.controller.exceptions.SenderApiException;
import jakarta.mail.internet.AddressException;

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
