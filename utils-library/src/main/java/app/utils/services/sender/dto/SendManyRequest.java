package app.utils.services.sender.dto;

public record SendManyRequest(
        String method,
        String contactId,
        String notificationName
) {
}
