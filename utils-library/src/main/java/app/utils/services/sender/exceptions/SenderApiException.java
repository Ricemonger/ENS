package app.utils.services.sender.exceptions;

public class SenderApiException extends RuntimeException {
    public SenderApiException() {
        super();
    }

    public SenderApiException(Throwable cause) {
        super(cause);
    }

    public SenderApiException(String message) {
        super(message);
    }
}
