package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.utils.ExceptionMessage;
import app.utils.services.security.exceptions.UserAlreadyExistsException;
import app.utils.services.security.exceptions.UserDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.config.request-mappings.ens}")
@RequiredArgsConstructor
public class EnsUserController {

    private final EnsUserControllerService ensUserControllerService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@RequestBody EnsUserRegisterRequest request) {
        return ensUserControllerService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody EnsUserLoginRequest request) {
        return ensUserControllerService.login(request);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidPasswordException(InvalidPasswordException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Password:\n" +
                "Password's format: 6-16 symbols, without {}[]():;'\".,<>/|\\ or space symbols");
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidUsernameException(InvalidUsernameException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Username:\n" +
                "Username's format: 6-24 symbols, only letters ,digits and \"_\" symbol allowed");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage badCredentialsException(BadCredentialsException e) {
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Wrong password entered, authorization is prohibited, please re-enter");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "User with same username already exists, please re-enter");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "User with such username doesn't exist, please re-enter");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage internalServerErrorOrUnknown(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN EXCEPTION");
    }
}
