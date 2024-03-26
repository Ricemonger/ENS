package app.telegram.bot.exceptions.internal;

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
}
