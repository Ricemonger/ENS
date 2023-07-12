package app.boot.authentication.user.controller.dto;

import app.boot.authentication.user.model.User;

public record UserLoginRequest(
        String username,
        String password
) {
    public User toUser(){
        return User
                .builder()
                .username(username)
                .password(password)
                .build();
    }
}
