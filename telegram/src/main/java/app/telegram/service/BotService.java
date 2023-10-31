package app.telegram.service;

import app.security.tg_users.database.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    public TelegramUser create(Long chatId) {
        return telegramUserService.create(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void clear(Long chatId) {
        contactService.clear(chatId);
        notificationService.clear(chatId);
    }

    public void sendAll(Long chatId) {
        //TODO SEND ALL
    }

    public void link(Long chatId, String username, String password) {
        //TODO LINKING
    }

    public void unlink(Long chatId) {

    }

    public void removeAllData(Long chatId) {

    }

    public void removeMostData(Long chatId) {

    }

    public String getUserData() {
        return null;
    }

    public boolean isLinked(Long chatId) {
        return false;
    }
}
