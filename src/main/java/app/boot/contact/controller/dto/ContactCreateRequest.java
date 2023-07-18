package app.boot.contact.controller.dto;

public record ContactCreateRequest(
        String method,
        String contactId
) {
}
