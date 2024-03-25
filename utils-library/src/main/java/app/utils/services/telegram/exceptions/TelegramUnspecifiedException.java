package app.utils.services.telegram.exceptions;

public class TelegramUnspecifiedException extends RuntimeException {
    public TelegramUnspecifiedException() {
        super();
    }

    public TelegramUnspecifiedException(Throwable cause) {
        super(cause);
    }

    public TelegramUnspecifiedException(String message) {
        super(message);
    }
}
