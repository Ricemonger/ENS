package app.utils.feign_clients.notification.dto;


public record NotificationCreUpdRequest(
        String name,
        String text
) {
}
