package app.utils.services.security.exceptions;

public class SecurityUnspecifiedException extends RuntimeException {
    public SecurityUnspecifiedException() {
        super();
    }

    public SecurityUnspecifiedException(Throwable cause) {
        super(cause);
    }

    public SecurityUnspecifiedException(String message) {
        super(message);
    }
}
