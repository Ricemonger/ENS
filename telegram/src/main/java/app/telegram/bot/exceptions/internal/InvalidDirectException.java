package app.telegram.bot.exceptions.internal;

import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidDirectException extends InternalErrorException {
    public InvalidDirectException() {
        super();
    }

    public InvalidDirectException(Throwable cause) {
        super(cause);
    }

    public InvalidDirectException(String message) {
        super(message);
    }

    public InvalidDirectException(Update update) {
        super(update);
    }
}
