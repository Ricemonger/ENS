package app.telegram.bot.exceptions.user;

public class InvalidDirectCommandException extends UserErrorException {
    public InvalidDirectCommandException() {
        super();
    }

    public InvalidDirectCommandException(Throwable cause) {
        super(cause);
    }

    public InvalidDirectCommandException(String message) {
        super(message);
    }
}
