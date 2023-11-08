package app.security.ens_users.controller;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.ens_users.EnsUser;
import app.security.ens_users.controller.dto.EnsUserAccountIdRequest;
import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.controller.dto.EnsUserUsernameRequest;
import app.security.ens_users.exceptions.InvalidPasswordException;
import app.security.ens_users.exceptions.InvalidUsernameException;
import app.security.ens_users.service.db.EnsUserRepositoryService;
import app.utils.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class EnsUserController {

    private final EnsUserRepositoryService ensUserRepositoryService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@RequestBody EnsUserRegisterRequest request) {
        String token = ensUserRepositoryService.register(request.toUser());
        log.trace("register method was called with request-{} and result-{}", request, token);
        return token;
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody EnsUserLoginRequest request) {
        String token = ensUserRepositoryService.login(request.toUser());
        log.trace("login method was called with request-{} and result-{}", request, token);
        return token;
    }

    @GetMapping("/getByUN")
    @ResponseStatus(value = HttpStatus.OK)
    public EnsUser getByUsername(EnsUserUsernameRequest request) {
        EnsUser result = ensUserRepositoryService.getByUsername(request.username());
        log.trace("getByUsername method was called with request-{} and result-{}", request, result);
        return result;
    }

    @GetMapping("/getByAI")
    @ResponseStatus(value = HttpStatus.OK)
    public EnsUser getByAccountId(EnsUserAccountIdRequest request) {
        EnsUser result = ensUserRepositoryService.getByAccountId(request.accountId());
        log.trace("getByAccountId method was called with request-{} and result-{}", request, result);
        return result;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        log.warn("UserAlreadyExistsException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "User with same username already exists, please re-enter");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        log.warn("NoSuchElementException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "User with such username doesn't exist, please re-enter");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidPasswordException(InvalidPasswordException e) {
        log.warn("InvalidPasswordException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Password:\n" +
                "Password's format: 6-16 symbols, without {}[]():;'\".,<>/|\\ or space symbols");
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidUsernameException(InvalidUsernameException e) {
        log.warn("InvalidUsernameException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Invalid Username:\n" +
                "Username's format: 6-24 symbols, only letters ,digits and \"_\" symbol allowed");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage badCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException was thrown");
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
