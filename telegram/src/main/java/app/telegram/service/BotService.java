package app.telegram.service;

import app.telegram.security.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    public void create(Long chatId) {
        telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void sendAll(Long chatId) {

    }

    public void unlink(Long chatId) {


    }

    public void link(Long chatId, String username, String password) {

    }

    public String getUserData() {


    }

    public void removeMostData(Long chatId) {

    }

    public void removeAllData(Long chatId) {

    }

    public void clear(Long chatId) {

    }

    public boolean isLinked(Long chatId) {
        return false;
    }
}
