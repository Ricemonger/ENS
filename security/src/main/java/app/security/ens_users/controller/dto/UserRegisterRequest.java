package app.security.ens_users.controller.dto;

import app.security.ens_users.EnsUser;

public record UserRegisterRequest(
        String username,
        String password
) {
    public EnsUser toUser() {
        return EnsUser
                .builder()
                .username(username)
                .password(password)
                .build();
    }
}
