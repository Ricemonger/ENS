package app.notification.exceptions;

public class NotificationAlreadyExistsException extends RuntimeException {
    public NotificationAlreadyExistsException() {
        super();
    }

    public NotificationAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public NotificationAlreadyExistsException(String message) {
        super(message);
    }
}
