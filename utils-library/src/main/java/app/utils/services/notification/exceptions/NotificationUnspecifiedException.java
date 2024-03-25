package app.utils.services.notification.exceptions;

public class NotificationUnspecifiedException extends RuntimeException {
    public NotificationUnspecifiedException() {
        super();
    }

    public NotificationUnspecifiedException(Throwable cause) {
        super(cause);
    }

    public NotificationUnspecifiedException(String message) {
        super(message);
    }
}
