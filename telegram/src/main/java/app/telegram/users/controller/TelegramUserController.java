package app.telegram.users.controller;

import app.telegram.users.exceptions.InvalidTelegramTokenException;
import app.utils.ExceptionMessage;
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
}
