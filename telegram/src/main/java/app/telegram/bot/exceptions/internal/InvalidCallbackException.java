package app.telegram.bot.exceptions.internal;

import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidCallbackException extends InternalErrorException {
    public InvalidCallbackException() {
        super();
    }

    public InvalidCallbackException(Throwable cause) {
        super(cause);
    }

    public InvalidCallbackException(String message) {
        super(message);
    }

    public InvalidCallbackException(Update update) {
        super(update);
    }
}
