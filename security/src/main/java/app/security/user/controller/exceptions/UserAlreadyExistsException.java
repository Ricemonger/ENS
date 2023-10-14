package app.security.user.controller.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
