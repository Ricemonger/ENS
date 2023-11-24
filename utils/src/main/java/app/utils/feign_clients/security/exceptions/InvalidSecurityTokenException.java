package app.utils.feign_clients.security.exceptions;

public class InvalidSecurityTokenException extends RuntimeException {
    public InvalidSecurityTokenException() {
        super();
    }

    public InvalidSecurityTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidSecurityTokenException(String message) {
        super(message);
    }
}
