package app.security.ens_users.exceptions;

public class InvalidIdsException extends RuntimeException {
    public InvalidIdsException() {
        super();
    }

    public InvalidIdsException(Throwable cause) {
        super(cause);
    }

    public InvalidIdsException(String message) {
        super(message);
    }
}
