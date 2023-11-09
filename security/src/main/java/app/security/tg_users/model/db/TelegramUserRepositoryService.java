package app.security.tg_users.model.db;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.tg_users.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserRepositoryService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser createOrThrow(TelegramUser user) {
        if (!doesUserExistByChatId(user.getChatId())) {
            TelegramUser result = telegramUserRepository.save(user);
            log.trace("create method was called with request-{} and result-{}", user, result);
            return result;
        } else {
            log.error("UserAlreadyExistsException was thrown during create method for user-{}", user);
            throw new UserAlreadyExistsException();
        }
    }

    public TelegramUser deleteOrThrow(TelegramUser user) {
        if (doesUserExistByChatId(user.getChatId())) {
            TelegramUser deleted = telegramUserRepository.findById(user.getChatId()).orElseThrow();
            telegramUserRepository.delete(user);
            log.trace("delete method was called for user-{} with result:{}", user, deleted);
            return deleted;
        } else if (doesUserExistByAccountId(user.getAccountId())) {
            TelegramUser deleted = telegramUserRepository.findByAccountId(user.getAccountId()).orElseThrow();
            telegramUserRepository.delete(user);
            log.trace("delete method was called for user-{} with result:{}", user, deleted);
            return deleted;
        } else {
            log.error("UserDoesntExistsException was thrown during delete method for user-{}", user);
            throw new UserDoesntExistException();
        }
    }

    public TelegramUser getByAccountIdOrThrow(String accountId) {
        if (doesUserExistByAccountId(accountId)) {
            TelegramUser result = telegramUserRepository.findByAccountId(accountId).orElseThrow();
            log.trace("getByAccountId method was called for accountId-{} with result:{}", accountId, result);
            return result;
        } else {
            log.error("UserDoesntExistsException was thrown during getByAccountId method for accountID-{}", accountId);
            throw new UserDoesntExistException();
        }
    }

    public TelegramUser getByChatIdOrThrow(String chatId) {
        if (doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepository.findById(chatId).orElseThrow();
            log.trace("getByChatId method was called for chatId-{} with result:{}", chatId, result);
            return result;
        } else {
            log.error("UserDoesntExistsException was thrown during getByChatId method for chatID-{}", chatId);
            throw new UserDoesntExistException();
        }
    }

    private boolean doesUserExistByChatId(String chatId) {
        boolean byChatId = telegramUserRepository.existsById(chatId);
        log.trace("doesUserExistByChatId method was called for user-{} with result:{}", chatId, byChatId);
        return byChatId;
    }

    private boolean doesUserExistByAccountId(String accountId) {
        boolean byChatId = telegramUserRepository.existsByAccountId(accountId);
        log.trace("doesUserExistByAccountId method was called for user-{} with result:{}", accountId, byChatId);
        return byChatId;
    }
}

