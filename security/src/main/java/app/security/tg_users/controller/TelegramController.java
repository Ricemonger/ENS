package app.security.tg_users.controller;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.controller.dto.TelegramUserAccountIdRequest;
import app.security.tg_users.controller.dto.TelegramUserChangeAccountIdRequest;
import app.security.tg_users.controller.dto.TelegramUserChatIdRequest;
import app.security.tg_users.service.database.TelegramUserService;
import app.utils.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tg-users")
@RequiredArgsConstructor
@Slf4j
public class TelegramController {

    private final TelegramUserService telegramUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TelegramUser create(TelegramUserChatIdRequest request) {
        System.out.println(request.chatId());
        TelegramUser user = new TelegramUser(request.chatId());
        TelegramUser result = telegramUserService.create(user);
        log.trace("create method was called with request-{} and result-{}", request, result);
        return result;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public TelegramUser delete(TelegramUserChatIdRequest request) {
        TelegramUser user = new TelegramUser(request.chatId());
        TelegramUser result = telegramUserService.delete(user);
        log.trace("delete method was called with request-{} and result-{}", request, result);
        return result;
    }

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.OK)
    public TelegramUser linkToEns(TelegramUserChangeAccountIdRequest request) {
        TelegramUser result = telegramUserService.changeAccountId(request.oldAccountId(), request.newAccountId());
        log.trace("linkToEns method was called with request-{} and result-{}", request, result);
        return result;
    }

    @GetMapping("/getByAI")
    @ResponseStatus(HttpStatus.OK)
    public TelegramUser getByAccountId(TelegramUserAccountIdRequest request) {
        String accountId = request.accountId();
        TelegramUser result = telegramUserService.getByAccountId(accountId);
        log.trace("getByAccountId method was called with request-{} and result-{}", request, result);
        return result;
    }

    @GetMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public TelegramUser getByChatId(TelegramUserChatIdRequest request) {
        String accountId = request.chatId();
        TelegramUser result = telegramUserService.getByChatId(accountId);
        log.trace("delete method was called with request-{} and result-{}", request, result);
        return result;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage alreadyExists(UserAlreadyExistsException e) {
        log.warn("UserAlreadyExistsException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "User with same chatID or accountID already exists");
    }

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage doesntExist(UserDoesntExistException e) {
        log.warn("NoSuchElementException was thrown");
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "User with such chatID or accountID doesnt exist");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

}
