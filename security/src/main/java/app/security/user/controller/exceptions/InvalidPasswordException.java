package app.security.user.controller.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
