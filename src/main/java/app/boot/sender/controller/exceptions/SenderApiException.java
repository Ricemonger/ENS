package app.boot.sender.controller.exceptions;

public class SenderApiException extends RuntimeException{
    public SenderApiException(){
        super();
    }
    public SenderApiException(String message){
        super(message);
    }
}
