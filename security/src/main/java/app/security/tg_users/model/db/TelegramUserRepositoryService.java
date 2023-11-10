package app.security.tg_users.model.db;

import app.security.tg_users.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserRepositoryService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser save(TelegramUser user) {
        return toTelegramUser(telegramUserRepository.save(toTelegramUserEntity(user)));
    }

    public void delete(TelegramUser user) {
        telegramUserRepository.delete(toTelegramUserEntity(user));
    }

    public TelegramUser findByChatIdOrThrow(String chatId) {
        return toTelegramUser(telegramUserRepository.findById(chatId).orElseThrow());
    }

    public TelegramUser findByAccountIdOrThrow(String accountId) {
        return toTelegramUser(telegramUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
    }

    public boolean existsByChatId(String chatId) {
        return telegramUserRepository.existsById(chatId);
    }

    public boolean existsByAccountId(String accountId) {
        return telegramUserRepository.existsByAnyUserEntityAccountId(accountId);
    }

    private TelegramUser toTelegramUser(TelegramUserEntity entity) {
        return new TelegramUser(entity);
    }

    private TelegramUserEntity toTelegramUserEntity(TelegramUser telegramUser) {
        return new TelegramUserEntity(telegramUser.getAccountId(), telegramUser.getChatId());
    }
}

