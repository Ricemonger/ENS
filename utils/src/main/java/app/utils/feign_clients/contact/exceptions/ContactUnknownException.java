package app.utils.feign_clients.contact.exceptions;

public class ContactUnknownException extends RuntimeException {
    public ContactUnknownException() {
        super();
    }

    public ContactUnknownException(Throwable cause) {
        super(cause);
    }

    public ContactUnknownException(String message) {
        super(message);
    }
}
