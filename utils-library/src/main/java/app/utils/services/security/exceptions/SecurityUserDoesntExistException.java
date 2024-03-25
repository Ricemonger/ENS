package app.utils.services.security.exceptions;

public class SecurityUserDoesntExistException extends RuntimeException {
    public SecurityUserDoesntExistException() {
        super();
    }

    public SecurityUserDoesntExistException(Throwable cause) {
        super(cause);
    }

    public SecurityUserDoesntExistException(String message) {
        super(message);
    }
}
