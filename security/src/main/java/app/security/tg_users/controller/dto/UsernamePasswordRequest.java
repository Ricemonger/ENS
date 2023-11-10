package app.security.tg_users.controller.dto;

public record UsernamePasswordRequest(
        String username,
        String password
) {
    public UsernamePasswordRequest toUser() {

    }
}
