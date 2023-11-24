package app.security.tg_users.model.db;

import app.security.tg_users.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserRepositoryService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser save(TelegramUser user) {
        TelegramUser result = toTelegramUser(telegramUserRepository.save(toTelegramUserEntity(user)));
        log.trace("save was executed with user-{} and result-{}", user, result);
        return result;
    }

    public void delete(TelegramUser user) {
        log.trace("delete was called for user-{}", user);
        telegramUserRepository.delete(toTelegramUserEntity(user));
    }

    public void deleteAll() {
        log.trace("deleteAll was called");
        telegramUserRepository.deleteAll();
    }

    public boolean existsByChatId(String chatId) {
        boolean result = telegramUserRepository.existsById(chatId);
        log.trace("existsByChatId was executed for chatId-{} with result-{}", chatId, result);
        return result;
    }

    public boolean existsByAccountId(String accountId) {
        boolean result = telegramUserRepository.existsByAnyUserEntityAccountId(accountId);
        log.trace("existsByAccountId was executed for accountId-{} with result-{}", accountId, result);
        return result;
    }

    public TelegramUser findByChatIdOrThrow(String chatId) {
        log.trace("findByChatIdOrThrow was called for chatId-{}", chatId);
        return toTelegramUser(telegramUserRepository.findById(chatId).orElseThrow());
    }

    public TelegramUser findByAccountIdOrThrow(String accountId) {
        log.trace("findByAccountIdOrThrow was called for accountId-{}", accountId);
        return toTelegramUser(telegramUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
    }

    public List<TelegramUser> findAll() {
        log.trace("findAll was called");
        return telegramUserRepository.findAll().stream().map(TelegramUser::new).collect(Collectors.toList());
    }

    private TelegramUser toTelegramUser(TelegramUserEntity entity) {
        return new TelegramUser(entity);
    }

    private TelegramUserEntity toTelegramUserEntity(TelegramUser telegramUser) {
        return new TelegramUserEntity(telegramUser.getAccountId(), telegramUser.getChatId());
    }
}

