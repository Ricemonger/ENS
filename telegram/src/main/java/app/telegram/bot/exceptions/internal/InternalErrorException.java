package app.telegram.bot.exceptions.internal;

import app.telegram.bot.exceptions.TelegramErrorException;

public class InternalErrorException extends TelegramErrorException {
    public InternalErrorException() {
        super();
    }

    public InternalErrorException(Throwable cause) {
        super(cause);
    }

    public InternalErrorException(String message) {
        super(message);
    }
}
