package app.notification.controller.exceptions;

public class NotificationDoesntExistException extends RuntimeException {
    public NotificationDoesntExistException() {
        super();
    }

    public NotificationDoesntExistException(Throwable cause) {
        super(cause);
    }

    public NotificationDoesntExistException(String message) {
        super(message);
    }
}
