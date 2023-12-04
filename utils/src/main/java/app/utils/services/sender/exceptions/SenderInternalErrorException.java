package app.utils.services.sender.exceptions;

public class SenderInternalErrorException extends RuntimeException {
    public SenderInternalErrorException() {
        super();
    }

    public SenderInternalErrorException(Throwable cause) {
        super(cause);
    }

    public SenderInternalErrorException(String message) {
        super(message);
    }
}
