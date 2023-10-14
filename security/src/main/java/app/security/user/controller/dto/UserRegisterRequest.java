package app.security.user.controller.dto;

import app.security.user.model.User;

public record UserRegisterRequest(
        String username,
        String password
) {
    public User toUser() {
        return User
                .builder()
                .username(username)
                .password(password)
                .build();
    }
}

