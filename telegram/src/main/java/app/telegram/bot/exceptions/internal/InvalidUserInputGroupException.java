package app.telegram.bot.exceptions.internal;

import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidUserInputGroupException extends InternalErrorException {
    public InvalidUserInputGroupException() {
        super();
    }

    public InvalidUserInputGroupException(Throwable cause) {
        super(cause);
    }

    public InvalidUserInputGroupException(String message) {
        super(message);
    }

    public InvalidUserInputGroupException(Update update) {
        super(update);
    }
}
