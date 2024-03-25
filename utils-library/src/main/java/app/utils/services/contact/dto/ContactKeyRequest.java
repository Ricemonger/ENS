package app.utils.services.contact.dto;

public record ContactKeyRequest(
        String method,
        String contactId
) {
}
