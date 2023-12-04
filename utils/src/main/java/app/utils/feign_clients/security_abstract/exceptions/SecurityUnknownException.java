package app.utils.feign_clients.security_abstract.exceptions;

public class SecurityUnknownException extends RuntimeException {
    public SecurityUnknownException() {
        super();
    }

    public SecurityUnknownException(Throwable cause) {
        super(cause);
    }

    public SecurityUnknownException(String message) {
        super(message);
    }
}
