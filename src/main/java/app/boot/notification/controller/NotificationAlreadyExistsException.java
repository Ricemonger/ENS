package app.boot.notification.controller;

public class NotificationAlreadyExistsException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Notification with same pair of creator's username and notification's name already exists";
    }
}
