package app.contact.controller.exceptions;

public class ContactDoesntExistException extends RuntimeException{
    public ContactDoesntExistException(){
        super();
    }
    public ContactDoesntExistException(Throwable cause){
        super(cause);
    }
    public ContactDoesntExistException(String message){
        super(message);
    }
}
