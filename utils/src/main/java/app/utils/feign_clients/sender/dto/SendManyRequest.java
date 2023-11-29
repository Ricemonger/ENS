package app.utils.feign_clients.sender.dto;

public record SendManyRequest(
        String method,
        String contactId,
        String notificationName
) {
}
