package app.boot.security.user.controller.dto;


import app.boot.security.user.model.User;

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
