package app.telegram.bot.exceptions;

public class EmptyInputMapException extends RuntimeException {
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
