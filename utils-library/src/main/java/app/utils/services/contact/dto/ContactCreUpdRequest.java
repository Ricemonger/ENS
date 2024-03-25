package app.utils.services.contact.dto;

public record ContactCreUpdRequest(
        String method,
        String contactId,
        String notificationName
) {
}
