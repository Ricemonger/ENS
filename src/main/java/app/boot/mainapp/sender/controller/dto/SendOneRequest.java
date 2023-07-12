package app.boot.mainapp.sender.controller.dto;

public record SendOneRequest(
        String method,
        String contactId
) {
}
