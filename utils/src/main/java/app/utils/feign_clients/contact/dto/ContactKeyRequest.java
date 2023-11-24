package app.utils.feign_clients.contact.dto;

public record ContactKeyRequest(
        String method,
        String contactId
) {
}
