package app.utils.services.notification.exceptions;

public class NotificationInternalServerError extends RuntimeException {
    public NotificationInternalServerError() {
        super();
    }

    public NotificationInternalServerError(Throwable cause) {
        super(cause);
    }

    public NotificationInternalServerError(String message) {
        super(message);
    }
}
