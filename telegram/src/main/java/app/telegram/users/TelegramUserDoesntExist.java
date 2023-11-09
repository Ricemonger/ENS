package app.telegram.users;

public class TelegramUserDoesntExist extends RuntimeException {
    public TelegramUserDoesntExist() {
        super();
    }

    public TelegramUserDoesntExist(Throwable cause) {
        super(cause);
    }

    public TelegramUserDoesntExist(String message) {
        super(message);
    }
}
