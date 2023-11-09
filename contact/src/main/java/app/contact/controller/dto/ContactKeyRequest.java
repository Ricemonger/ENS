package app.contact.controller.dto;

public record ContactKeyRequest(
        String method,
        String contactId
) {
}
