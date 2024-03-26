package app.telegram.bot.exceptions.internal;

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
}
