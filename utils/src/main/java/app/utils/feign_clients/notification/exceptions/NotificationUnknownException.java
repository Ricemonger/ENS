package app.utils.feign_clients.notification.exceptions;

public class NotificationUnknownException extends RuntimeException {
    public NotificationUnknownException() {
        super();
    }

    public NotificationUnknownException(Throwable cause) {
        super(cause);
    }

    public NotificationUnknownException(String message) {
        super(message);
    }
}
