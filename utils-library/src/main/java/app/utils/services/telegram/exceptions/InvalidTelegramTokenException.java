package app.utils.services.telegram.exceptions;

public class InvalidTelegramTokenException extends RuntimeException {
    public InvalidTelegramTokenException() {
        super();
    }

    public InvalidTelegramTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidTelegramTokenException(String message) {
        super(message);
    }
}
