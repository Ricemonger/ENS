package app.boot.mainapp.contact.controller;

public record ContactKeyRequest(
        String method,
        String contactId
) {
}
