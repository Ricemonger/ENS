package app.utils.services.security.telegram.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
}
