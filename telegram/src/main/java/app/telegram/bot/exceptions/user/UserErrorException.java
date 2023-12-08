package app.telegram.bot.exceptions.user;

import app.telegram.bot.exceptions.TelegramErrorException;

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
}
