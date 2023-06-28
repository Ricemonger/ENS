package app.boot.mainapp.contact.controller;

public record ContactCreUpdRequest(
        String method,
        String contactId,
        String notificationName
) {
}
