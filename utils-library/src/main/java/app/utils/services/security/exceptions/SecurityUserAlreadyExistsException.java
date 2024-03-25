package app.utils.services.security.exceptions;

public class SecurityUserAlreadyExistsException extends RuntimeException {
    public SecurityUserAlreadyExistsException() {
        super();
    }

    public SecurityUserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public SecurityUserAlreadyExistsException(String message) {
        super(message);
    }
}
