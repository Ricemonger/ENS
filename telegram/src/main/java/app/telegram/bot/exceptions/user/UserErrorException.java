package app.telegram.bot.exceptions.user;

import app.telegram.bot.exceptions.TelegramErrorException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserErrorException extends TelegramErrorException {
    public UserErrorException() {
        super();
    }

    public UserErrorException(Throwable cause) {
        super(cause);
    }

    public UserErrorException(String message) {
        super(message);
    }

    public UserErrorException(Update update) {
        super(update);
    }
}
