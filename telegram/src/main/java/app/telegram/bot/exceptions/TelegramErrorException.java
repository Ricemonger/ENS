package app.telegram.bot.exceptions;

public class TelegramErrorException extends RuntimeException {
    public TelegramErrorException() {
        super();
    }

    public TelegramErrorException(Throwable cause) {
        super(cause);
    }

    public TelegramErrorException(String message) {
        super(message);
    }
}
