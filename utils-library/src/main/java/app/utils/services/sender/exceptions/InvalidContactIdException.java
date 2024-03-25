package app.utils.services.sender.exceptions;

public class InvalidContactIdException extends RuntimeException {
    public InvalidContactIdException() {
        super();
    }

    public InvalidContactIdException(Throwable cause) {
        super(cause);
    }

    public InvalidContactIdException(String message) {
        super(message);
    }
}
