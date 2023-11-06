package app.utils.sender;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
