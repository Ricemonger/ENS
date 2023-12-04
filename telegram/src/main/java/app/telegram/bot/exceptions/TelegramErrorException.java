package app.telegram.bot.exceptions;

import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramErrorException extends RuntimeException {

    private Update update;

    public TelegramErrorException() {
        super();
    }

    public TelegramErrorException(Throwable cause) {
        super(cause);
    }

    public TelegramErrorException(String message) {
        super(message);
    }

    public TelegramErrorException(Update update) {
        this.update = update;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
