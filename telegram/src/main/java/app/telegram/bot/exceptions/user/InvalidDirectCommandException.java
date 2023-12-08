package app.telegram.bot.exceptions.user;

import org.telegram.telegrambots.meta.api.objects.Update;

public class InvalidDirectCommandException extends UserErrorException {
    public InvalidDirectCommandException() {
        super();
    }

    public InvalidDirectCommandException(Throwable cause) {
        super(cause);
    }

    public InvalidDirectCommandException(String message) {
        super(message);
    }

    public InvalidDirectCommandException(Update update) {
        super(update);
    }
}
