package app.telegram.service;

import app.telegram.service.database.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser create(Long chatId) {
        String chatNumber = String.valueOf(chatId);
        return create(chatNumber);
    }

    public TelegramUser create(String chatId) {
        TelegramUser telegramUser = TelegramUser.fromChatId(chatId);
        if (!telegramUserRepository.existsById(chatId)) {
            return telegramUserRepository.save(telegramUser);
        } else
            throw new TelegramUserAlreadyExistsException();
    }

    public boolean doesUserExist(String chatId) {
        return telegramUserRepository.existsById(chatId);
    }

    public boolean doesUserExist(Long chatId) {
        return telegramUserRepository.existsById(String.valueOf(chatId));
    }
}

