package app.utils.services.sender.exceptions;

public class SenderUnspecifiedException extends RuntimeException {
    public SenderUnspecifiedException() {
        super();
    }

    public SenderUnspecifiedException(Throwable cause) {
        super(cause);
    }

    public SenderUnspecifiedException(String message) {
        super(message);
    }
}
