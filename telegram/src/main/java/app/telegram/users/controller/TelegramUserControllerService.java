package app.telegram.users.controller;

import app.telegram.users.model.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserControllerService {

    private final TelegramUserService telegramUserService;

    public String getChatIdByToken(String telegramToken) {
        log.trace("getChatIdByToken was called with token-{}", telegramToken);
        return telegramUserService.getChatIdByToken(telegramToken);
    }
}
