package app.utils.feign_clients.security_abstract.exceptions;

public class SecurityInternalServerError extends RuntimeException {
    public SecurityInternalServerError() {
        super();
    }

    public SecurityInternalServerError(Throwable cause) {
        super(cause);
    }

    public SecurityInternalServerError(String message) {
        super(message);
    }
}
