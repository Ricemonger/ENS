package app.utils.feign_clients.security.exceptions;

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
