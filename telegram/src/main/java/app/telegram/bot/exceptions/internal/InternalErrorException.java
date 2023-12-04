package app.telegram.bot.exceptions.internal;

import app.telegram.bot.exceptions.TelegramErrorException;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    public InternalErrorException(Update update) {
        super(update);
    }
}
