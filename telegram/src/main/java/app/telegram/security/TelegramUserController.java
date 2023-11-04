package app.telegram.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram/security")
@RequiredArgsConstructor
public class TelegramUserController {

    private final TelegramUserService telegramUserService;

    @GetMapping("/getChatId")
    public String getChatId(@RequestHeader("Authorization") String telegramToken) {
        return telegramUserService.getChatIdByToken(telegramToken);
    }
}
