package app.telegram.users.model.db;

import app.telegram.users.model.TelegramUser;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
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
        return toUser(repository.save(toEntity(telegramUser)));
    }

    public void delete(TelegramUser telegramUser) {
        repository.delete(toEntity(telegramUser));
    }

    public TelegramUser findByChatIdOrThrow(String chatId) {
        try {
            return toUser(repository.findById(chatId).orElseThrow());
        } catch (NoSuchElementException e) {
            log.info("findByChatId was executed with chatId-{}, result wasn't found in DB", chatId);
            throw new TelegramUserDoesntExistException();
        }
    }

    public List<TelegramUser> findAll() {
        return repository.findAll().stream().map(this::toUser).toList();
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    private TelegramUserEntity toEntity(TelegramUser telegramUser) {
        return new TelegramUserEntity(telegramUser);
    }

    private TelegramUser toUser(TelegramUserEntity entity) {
        return new TelegramUser(entity);
    }
}
