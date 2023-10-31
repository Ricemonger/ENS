package app.security.user.controller.dto;


import app.security.user.service.ens_user.EnsUser;

public record UserLoginRequest(
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
