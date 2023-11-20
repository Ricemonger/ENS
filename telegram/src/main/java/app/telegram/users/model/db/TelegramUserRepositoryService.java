package app.telegram.users.model.db;

import app.telegram.users.exceptions.TelegramUserDoesntExistException;
import app.telegram.users.model.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserRepositoryService {

    private final TelegramUserRepository repository;

    public TelegramUser save(TelegramUser telegramUser) {
        TelegramUserEntity saved = repository.save(toEntity(telegramUser));
        log.trace("Save method was called for user-{}", telegramUser);
        return toUser(saved);
    }

    public void delete(TelegramUser telegramUser) {
        repository.delete(toEntity(telegramUser));
        log.trace("Delete method was called for user-{}", telegramUser);
    }

    public TelegramUser findByChatIdOrThrow(String chatId) {
        try {
            TelegramUserEntity entity = repository.findById(chatId).orElseThrow();
            log.trace("findByChatId was executed with chatId-{}, result-{}", chatId, entity);
            return toUser(entity);
        } catch (NoSuchElementException e) {
            log.trace("findByChatId was executed with chatId-{}, result wasn't found in DB", chatId);
            throw new TelegramUserDoesntExistException();
        }
    }

    public List<TelegramUser> findAll() {
        return repository.findAll().stream().map(this::toUser).toList();
    }

    public void deleteAll() {
        repository.deleteAll();
        log.trace("DeleteAll method was called for repository");
    }

    private TelegramUserEntity toEntity(TelegramUser telegramUser) {
        return new TelegramUserEntity(
                telegramUser.getChatId(),
                telegramUser.getTempTelegramToken(),
                telegramUser.getTempTelegramTokenExpirationTime(),
                telegramUser.getTempSecurityToken(),
                telegramUser.getTempSecurityTokenExpirationTime());
    }

    private TelegramUser toUser(TelegramUserEntity entity) {
        return new TelegramUser(entity);
    }
}
