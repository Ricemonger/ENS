package app.telegram.bot.exceptions;

public class InternalTelegramErrorException extends RuntimeException{
    public InternalTelegramErrorException() {
        super();
    }

    public InternalTelegramErrorException(Throwable cause) {
        super(cause);
    }

    public InternalTelegramErrorException(String message) {
        super(message);
    }
}
