package app.boot.security.user.controller;

import app.boot.security.user.controller.dto.UserLoginRequest;
import app.boot.security.user.controller.dto.UserRegisterRequest;
import app.boot.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@RequestBody UserRegisterRequest request){
        return userService.register(request.toUser());
    }
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody UserLoginRequest request){
        return userService.login(request.toUser());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String alreadyExists(){
        return "User with same username already exists, please re-enter";
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String doesntExist(){
        return "User with such username doesn't exist, please re-enter";
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String wrongPassword(){
        return "Wrong password entered, authorization is prohibited, please re-enter";
    }
}
