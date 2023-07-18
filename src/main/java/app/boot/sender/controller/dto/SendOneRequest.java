package app.boot.sender.controller.dto;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
