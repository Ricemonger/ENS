package app.security.ens_users.exceptions;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException() {
        super();
    }

    public InvalidUsernameException(Throwable cause) {
        super(cause);
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}
