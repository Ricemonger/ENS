package app.telegram.users.controller;

import app.utils.ExceptionMessage;
import app.utils.services.telegram.exceptions.InvalidTelegramTokenException;
import app.utils.services.telegram.exceptions.TelegramUserAlreadyExistsException;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram/security")
@RequiredArgsConstructor
public class TelegramUserController {

    private final TelegramUserControllerService telegramUserControllerService;

    @RequestMapping("/getChatId")
    public String getChatId(@RequestHeader("Authorization") String telegramToken) {
        return telegramUserControllerService.getChatIdByToken(telegramToken);
    }

    @ExceptionHandler(InvalidTelegramTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage invalidTelegramToken(InvalidTelegramTokenException e) {
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Invalid Telegram Token!");
    }

    @ExceptionHandler(TelegramUserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage telegramUserAlreadyExists(TelegramUserAlreadyExistsException e) {
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Invalid Telegram Token!");
    }

    @ExceptionHandler(TelegramUserDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage telegramUserDoesntExist(TelegramUserDoesntExistException e) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "Invalid Telegram Token!");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage internalServerErrorOrUnknown(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    }
}
