package app.utils.services.sender.dto;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
