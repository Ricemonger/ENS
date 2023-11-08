package app.utils.feign_clients.sender;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
