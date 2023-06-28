package app.boot.authentication.user.controller;

import app.boot.authentication.user.model.User;

public record UserLoginRequest(
        String username,
        String password
) {
    User toUser(){
        return User
                .builder()
                .username(username)
                .password(password)
                .build();
    }
}
