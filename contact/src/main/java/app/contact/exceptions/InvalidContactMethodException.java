package app.contact.exceptions;

public class InvalidContactMethodException extends RuntimeException {
    public InvalidContactMethodException() {
        super();
    }

    public InvalidContactMethodException(Throwable cause) {
        super(cause);
    }

    public InvalidContactMethodException(String message) {
        super(message);
    }
}
