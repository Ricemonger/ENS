package app.telegram.bot.exceptions.internal;

import org.telegram.telegrambots.meta.api.objects.Update;

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

    public ListeningMethodCouldNotBeChosenException(Update update) {
        super(update);
    }
}
