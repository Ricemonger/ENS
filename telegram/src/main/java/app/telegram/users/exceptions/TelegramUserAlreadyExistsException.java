package app.telegram.users.exceptions;

public class TelegramUserAlreadyExistsException extends RuntimeException {
    public TelegramUserAlreadyExistsException() {
        super();
    }

    public TelegramUserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public TelegramUserAlreadyExistsException(String message) {
        super(message);
    }
}