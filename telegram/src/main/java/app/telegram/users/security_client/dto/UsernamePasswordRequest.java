package app.telegram.users.security_client.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
}
