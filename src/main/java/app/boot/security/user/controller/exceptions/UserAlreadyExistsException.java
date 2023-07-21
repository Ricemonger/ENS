package app.boot.security.user.controller.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    @Override
    public String getMessage() {
        return "User with same username already exists, please re-enter";
    }
}
