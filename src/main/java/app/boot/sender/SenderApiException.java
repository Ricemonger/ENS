package app.boot.sender;

public class SenderApiException extends RuntimeException{
    public SenderApiException(){
        super();
    }
    public SenderApiException(String message){
        super(message);
    }
}
