package app.telegram.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUserControllerService {

    private final TelegramUserService telegramUserService;

    public String getChatIdByToken(String telegramToken) {
        return telegramUserService.getChatIdByToken(telegramToken);
    }
}
