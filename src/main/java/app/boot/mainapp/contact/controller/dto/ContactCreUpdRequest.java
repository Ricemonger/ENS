package app.boot.mainapp.contact.controller.dto;

public record ContactCreUpdRequest(
        String method,
        String contactId,
        String notificationName
) {
}
