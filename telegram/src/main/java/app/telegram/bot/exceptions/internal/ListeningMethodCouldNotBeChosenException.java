package app.telegram.bot.exceptions.internal;

public class ListeningMethodCouldNotBeChosenException extends InternalErrorException {
    public ListeningMethodCouldNotBeChosenException() {
        super();
    }

    public ListeningMethodCouldNotBeChosenException(Throwable cause) {
        super(cause);
    }

    public ListeningMethodCouldNotBeChosenException(String message) {
        super(message);
    }
}
