package app.boot.contact.controller.dto;

public record ContactCreUpdRequest(
        String method,
        String contactId,
        String notificationName
) {
}
