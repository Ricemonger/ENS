package app.utils.feign_clients.contact;

public record ContactPKRequest(
        String method,
        String contactId
) {
}
