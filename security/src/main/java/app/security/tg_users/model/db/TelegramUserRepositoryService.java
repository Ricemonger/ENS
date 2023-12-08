package app.security.tg_users.model.db;

import app.security.tg_users.TelegramUser;
import app.utils.services.security.exceptions.SecurityUserDoesntExistException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TelegramUserRepositoryService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser save(TelegramUser user) {
        return toTelegramUser(telegramUserRepository.save(toTelegramUserEntity(user)));
    }

    public void delete(String chatId) {
        try {
            TelegramUserEntity toDelete = telegramUserRepository.getReferenceById(chatId);
            toDelete.getAnyUserEntity().setTelegramUserEntity(null);
            telegramUserRepository.delete(toDelete);
        } catch (EntityNotFoundException e) {
            throw new SecurityUserDoesntExistException(e);
        }
    }

    public void deleteAll() {
        telegramUserRepository.deleteAllInBatch();
    }

    public boolean existsByChatId(String chatId) {
        return telegramUserRepository.existsById(chatId);
    }

    public boolean existsByAccountId(String accountId) {
        return telegramUserRepository.existsByAnyUserEntityAccountId(accountId);
    }

    public TelegramUser findByChatIdOrThrow(String chatId) {
        return toTelegramUser(telegramUserRepository.findById(chatId).orElseThrow());
    }

    public TelegramUser findByAccountIdOrThrow(String accountId) {
        return toTelegramUser(telegramUserRepository.findByAnyUserEntityAccountId(accountId).orElseThrow());
    }

    public List<TelegramUser> findAll() {
        return telegramUserRepository.findAll().stream().map(TelegramUser::new).collect(Collectors.toList());
    }

    private TelegramUser toTelegramUser(TelegramUserEntity entity) {
        return new TelegramUser(entity);
    }

    private TelegramUserEntity toTelegramUserEntity(TelegramUser telegramUser) {
        return new TelegramUserEntity(telegramUser.getAccountId(), telegramUser.getChatId());
    }
}

