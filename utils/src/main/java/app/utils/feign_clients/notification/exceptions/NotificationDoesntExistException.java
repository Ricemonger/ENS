package app.utils.feign_clients.notification.exceptions;

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
