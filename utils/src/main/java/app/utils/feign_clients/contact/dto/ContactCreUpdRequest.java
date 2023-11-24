package app.utils.feign_clients.contact.dto;

public record ContactCreUpdRequest(
        String method,
        String contactId,
        String notificationName
) {
}
