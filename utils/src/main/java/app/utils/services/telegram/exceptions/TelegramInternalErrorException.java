package app.utils.services.telegram.exceptions;

public class TelegramInternalErrorException extends RuntimeException {
    public TelegramInternalErrorException() {
        super();
    }

    public TelegramInternalErrorException(Throwable cause) {
        super(cause);
    }

    public TelegramInternalErrorException(String message) {
        super(message);
    }
}
