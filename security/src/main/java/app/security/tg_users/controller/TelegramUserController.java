package app.security.tg_users.controller;

import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import app.utils.ExceptionMessage;
import app.utils.feign_clients.security.exceptions.UserAlreadyExistsException;
import app.utils.feign_clients.security.exceptions.UserDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.config.request-mappings.tg}")
@RequiredArgsConstructor
@Slf4j
public class TelegramUserController {

    private final TelegramUserControllerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("create method was called for telegramToken-{}", telegramToken);
        return service.create(telegramToken);
    }

    @GetMapping("/getSecurityToken")
    @ResponseStatus(HttpStatus.OK)
    public String getSecurityToken(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("getSecurityToken was called for telegramToken-{}", telegramToken);
        return service.getSecurityToken(telegramToken);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean doesUserExists(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("doesUserExists method was called for telegramToken-{}", telegramToken);
        return service.doesUserExists(telegramToken);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("delete was called for telegramToken-{}", telegramToken);
        service.delete(telegramToken);
    }

    @GetMapping("/getAccountInfo")
    @ResponseStatus(HttpStatus.OK)
    public String getAccountInfo(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("getAccountInfo was called for telegramToken-{}", telegramToken);
        return service.getAccountInfo(telegramToken);
    }

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public void link(@RequestHeader(name = "Authorization") String telegramToken,
                     @RequestBody UsernamePasswordRequest request) {
        log.trace("link was called for telegramToken-{} and request-{}", telegramToken, request);
        service.link(telegramToken, request);
    }

    @DeleteMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public void unlink(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("unlink was called for telegramToken-{}", telegramToken);
        service.unlinkWithDataToTelegram(telegramToken);
    }

    @GetMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public boolean isLinked(@RequestHeader(name = "Authorization") String telegramToken) {
        log.trace("isLinked was called for telegramToken-{}", telegramToken);
        return service.isLinked(telegramToken);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        log.warn("UserAlreadyExistsException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Same user already exists");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        log.warn("NoSuchElementException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Such user doesn't exist");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN EXCEPTION");
    }
}
