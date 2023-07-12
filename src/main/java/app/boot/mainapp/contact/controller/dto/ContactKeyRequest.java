package app.boot.mainapp.contact.controller.dto;

public record ContactKeyRequest(
        String method,
        String contactId
) {
}
