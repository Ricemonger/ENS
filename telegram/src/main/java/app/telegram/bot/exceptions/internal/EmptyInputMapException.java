package app.telegram.bot.exceptions.internal;

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
}
