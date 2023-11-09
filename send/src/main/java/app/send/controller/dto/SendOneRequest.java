package app.send.controller.dto;

public record SendOneRequest(
        String method,
        String contactId,
        String notificationText
) {
}
