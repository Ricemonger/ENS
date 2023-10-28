package app.telegram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramUserService telegramUserService;

    public TelegramUser create(Long chatId) {
        return telegramUserService.create(chatId);
    }

    public TelegramUser create(String chatId) {
        return telegramUserService.create(chatId);
    }

    public boolean doesUserExists(String chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public boolean doesUserExists(Long chatId) {
        return telegramUserService.doesUserExist(chatId);
    }

    public void clear(Long chatId) {
        //TODO CLEAR
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
}
