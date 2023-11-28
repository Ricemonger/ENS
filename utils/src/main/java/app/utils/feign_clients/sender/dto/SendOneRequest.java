package app.utils.feign_clients.sender.dto;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
