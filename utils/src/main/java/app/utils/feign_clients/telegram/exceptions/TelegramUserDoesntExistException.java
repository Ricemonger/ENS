package app.utils.feign_clients.telegram.exceptions;

public class TelegramUserDoesntExistException extends RuntimeException {
    public TelegramUserDoesntExistException() {
        super();
    }

    public TelegramUserDoesntExistException(Throwable cause) {
        super(cause);
    }

    public TelegramUserDoesntExistException(String message) {
        super(message);
    }
}
