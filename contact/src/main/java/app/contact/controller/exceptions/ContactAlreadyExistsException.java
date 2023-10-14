package app.contact.controller.exceptions;

public class ContactAlreadyExistsException extends RuntimeException {
    public ContactAlreadyExistsException() {
        super();
    }

    public ContactAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ContactAlreadyExistsException(String message) {
        super(message);
    }
}
