package app.security.user.controller.exceptions;

public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException() {
        super();
    }

    public UserDoesntExistException(Throwable cause) {
        super(cause);
    }

    public UserDoesntExistException(String message) {
        super(message);
    }
}
