package app.telegram.users.model.security_telegram_client.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
}
