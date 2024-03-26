package app.telegram.bot.exceptions.user;

public class InvalidTaskTypeException extends UserErrorException {
    public InvalidTaskTypeException() {
        super();
    }

    public InvalidTaskTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidTaskTypeException(String message) {
        super(message);
    }
}
