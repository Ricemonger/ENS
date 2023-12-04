package app.telegram.bot.exceptions.internal;

import org.telegram.telegrambots.meta.api.objects.Update;

public class EmptyInputMapException extends InternalErrorException {
    public EmptyInputMapException() {
        super();
    }

    public EmptyInputMapException(Throwable cause) {
        super(cause);
    }

    public EmptyInputMapException(String message) {
        super(message);
    }

    public EmptyInputMapException(Update update) {
        super(update);
    }
}
