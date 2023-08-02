package app.contact.controller.dto;

public record ContactCreateRequest(
        String method,
        String contactId
) {
}
