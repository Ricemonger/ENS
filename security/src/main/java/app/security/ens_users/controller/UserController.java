package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.UserLoginRequest;
import app.security.ens_users.controller.dto.UserRegisterRequest;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.security.ens_users.exceptions.UserAlreadyExistsException;
import app.security.ens_users.exceptions.UserDoesntExistException;
import app.security.ens_users.service.db.EnsUserService;
import app.utils.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EnsUserService ensUserService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@RequestBody UserRegisterRequest request) {
        log.trace("register method was called with params: username-{}", request.username());
        return ensUserService.register(request.toUser());
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody UserLoginRequest request) {
        log.trace("UserController's login method was called with params: username-{}", request.username());
        return ensUserService.login(request.toUser());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        log.warn("UserAlreadyExistsException of UserController was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "EnsUser with same username already exists, please re-enter");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        log.warn("NoSuchElementException of UserController was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "EnsUser with such username doesn't exist, please re-enter");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidPasswordException(InvalidPasswordException e) {
        log.warn("InvalidPasswordException of UserController was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Password:\n" +
                "Password's format: 6-16 symbols, without {}[]():;'\".,<>/|\\ or space symbols");
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidUsernameException(InvalidUsernameException e) {
        log.warn("InvalidUsernameException of UserController was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Username:\n" +
                "Username's format: 6-24 symbols, only letters ,digits and \"_\" symbol allowed");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage badCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException of UserController was thrown");
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Wrong password entered, authorization is prohibited, please re-enter");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}