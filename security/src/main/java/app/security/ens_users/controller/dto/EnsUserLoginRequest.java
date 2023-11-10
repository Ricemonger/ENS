package app.security.ens_users.controller.dto;


import app.security.ens_users.EnsUser;

public record EnsUserLoginRequest(
        String username,
        String password
) {
    public EnsUser toUser() {
        return new EnsUser(username, password);
    }
}
