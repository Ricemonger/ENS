package app.telegram.bot.exceptions.user;

public class InvalidTimeAndDateFormatInput extends UserErrorException {
    public InvalidTimeAndDateFormatInput() {
        super();
    }

    public InvalidTimeAndDateFormatInput(Throwable cause) {
        super(cause);
    }

    public InvalidTimeAndDateFormatInput(String message) {
        super(message);
    }
}
