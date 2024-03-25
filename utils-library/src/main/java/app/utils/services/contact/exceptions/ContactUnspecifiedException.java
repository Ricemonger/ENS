package app.utils.services.contact.exceptions;

public class ContactUnspecifiedException extends RuntimeException {
    public ContactUnspecifiedException() {
        super();
    }

    public ContactUnspecifiedException(Throwable cause) {
        super(cause);
    }

    public ContactUnspecifiedException(String message) {
        super(message);
    }
}
