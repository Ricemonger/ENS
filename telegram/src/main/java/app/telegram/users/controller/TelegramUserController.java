package app.telegram.users.controller;

import app.telegram.users.db.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram/security")
@RequiredArgsConstructor
public class TelegramUserController {

    private final TelegramUserService telegramUserService;

    @RequestMapping("/getChatId")
    public String getChatId(@RequestHeader("Authorization") String telegramToken) {
        return telegramUserService.getChatIdByToken(telegramToken);
    }
}
