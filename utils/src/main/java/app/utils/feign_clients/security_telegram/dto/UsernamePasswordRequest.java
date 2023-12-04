package app.utils.feign_clients.security_telegram.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
}
