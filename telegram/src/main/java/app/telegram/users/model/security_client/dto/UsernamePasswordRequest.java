package app.telegram.users.model.security_client.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
}
