package app.boot.security.user.controller;

import app.boot.security.user.controller.dto.UserLoginRequest;
import app.boot.security.user.controller.dto.UserRegisterRequest;
import app.boot.security.user.controller.exceptions.InvalidPasswordException;
import app.boot.security.user.controller.exceptions.InvalidUsernameException;
import app.boot.security.user.controller.exceptions.UserAlreadyExistsException;
import app.boot.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@RequestBody UserRegisterRequest request){
        log.trace("register method was called with params: username-{}",request.username());
        return userService.register(request.toUser());
    }
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody UserLoginRequest request){
        log.trace("UserController's login method was called with params: username-{}",request.username());
        return userService.login(request.toUser());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String alreadyExists(){
        log.warn("UserAlreadyExistsException of UserController was thrown");
        return "User with same username already exists, please re-enter";
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String doesntExist(){
        log.warn("NoSuchElementException of UserController was thrown");
        return "User with such username doesn't exist, please re-enter";
    }
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String InvalidPassword(){
        log.warn("InvalidPasswordException of UserController was thrown");
        return "Invalid Password:\n"+
                "Password's format: 6-16 symbols, without {}[]():;'\".,<>/|\\ or space symbols";
    }
    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String InvalidUsername(){
        log.warn("InvalidUsernameException of UserController was thrown");
        return "Invalid Username:\n"+
                "Username's format: 6-24 symbols, only letters ,digits and \"_\" symbol allowed";
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String wrongPassword(){
        log.warn("BadCredentialsException of UserController was thrown");
        return "Wrong password entered, authorization is prohibited, please re-enter";
    }

}
