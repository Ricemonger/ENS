package app.telegram.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram/security")
@RequiredArgsConstructor
@Slf4j
public class TelegramUserController {

    private final TelegramUserControllerService telegramUserControllerService;

    @RequestMapping("/getChatId")
    public String getChatId(@RequestHeader("Authorization") String telegramToken) {
        log.trace("getChatId method was called for token-{}", telegramToken);
        return telegramUserControllerService.getChatIdByToken(telegramToken);
    }
}
