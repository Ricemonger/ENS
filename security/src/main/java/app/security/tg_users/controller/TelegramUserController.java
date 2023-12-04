package app.security.tg_users.controller;

import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import app.utils.ExceptionMessage;
import app.utils.feign_clients.security_abstract.exceptions.InvalidSecurityTokenException;
import app.utils.feign_clients.security_abstract.exceptions.UserAlreadyExistsException;
import app.utils.feign_clients.security_abstract.exceptions.UserDoesntExistException;
import app.utils.feign_clients.telegram.exceptions.InvalidTelegramTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.config.request-mappings.tg}")
@RequiredArgsConstructor
public class TelegramUserController {

    private final TelegramUserControllerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestHeader(name = "Authorization") String telegramToken) {
        return service.create(telegramToken);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestHeader(name = "Authorization") String telegramToken) {
        service.delete(telegramToken);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean doesUserExists(@RequestHeader(name = "Authorization") String telegramToken) {
        return service.doesUserExists(telegramToken);
    }

    @GetMapping("/getSecurityToken")
    @ResponseStatus(HttpStatus.OK)
    public String getSecurityToken(@RequestHeader(name = "Authorization") String telegramToken) {
        return service.getSecurityToken(telegramToken);
    }

    @GetMapping("/getAccountInfo")
    @ResponseStatus(HttpStatus.OK)
    public String getAccountInfo(@RequestHeader(name = "Authorization") String telegramToken) {
        return service.getAccountInfo(telegramToken);
    }

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public void link(@RequestHeader(name = "Authorization") String telegramToken,
                     @RequestBody UsernamePasswordRequest request) {
        service.link(telegramToken, request);
    }

    @DeleteMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public void unlink(@RequestHeader(name = "Authorization") String telegramToken) {
        service.unlinkWithDataToTelegram(telegramToken);
    }

    @GetMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public boolean isLinked(@RequestHeader(name = "Authorization") String telegramToken) {
        return service.isLinked(telegramToken);
    }

    @ExceptionHandler(InvalidSecurityTokenException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage invalidTelegramToken(InvalidTelegramTokenException e) {
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Invalid or expired telegram jwt token");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Same user already exists");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "Such user doesn't exist");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    }
}
