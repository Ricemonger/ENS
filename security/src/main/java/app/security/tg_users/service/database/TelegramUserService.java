package app.security.tg_users.service.database;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.tg_users.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser create(TelegramUser user) {
        if (!doesUserExistByChatId(user.getChatId())) {
            TelegramUser result = telegramUserRepository.save(user);
            log.trace("create method was called with request-{} and result-{}", user, result);
            return result;
        } else {
            log.error("UserAlreadyExistsException was thrown during create method for user-{}", user);
            throw new UserAlreadyExistsException();
        }
    }

    public TelegramUser delete(TelegramUser user) {
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

    public TelegramUser getByAccountId(String accountId) {
        if (doesUserExistByAccountId(accountId)) {
            TelegramUser result = telegramUserRepository.findByAccountId(accountId).orElseThrow();
            log.trace("getByAccountId method was called for accountId-{} with result:{}", accountId, result);
            return result;
        } else {
            log.error("UserDoesntExistsException was thrown during getByAccountId method for accountID-{}", accountId);
            throw new UserDoesntExistException();
        }
    }

    public TelegramUser getByChatId(String chatId) {
        if (doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepository.findById(chatId).orElseThrow();
            log.trace("getByChatId method was called for chatId-{} with result:{}", chatId, result);
            return result;
        } else {
            log.error("UserDoesntExistsException was thrown during getByChatId method for chatID-{}", chatId);
            throw new UserDoesntExistException();
        }
    }

    public TelegramUser changeAccountId(String oldAccountId, String newAccountId) {
        if (!doesUserExistByAccountId(newAccountId)) {
            TelegramUser toChange = telegramUserRepository.findByAccountId(oldAccountId).orElseThrow();
            toChange.setAccountId(newAccountId);
            TelegramUser result = telegramUserRepository.save(toChange);
            log.trace("changeAccountId method was called with params:oldAccountId-{}, newAccountId-{}. and result-{}"
                    , oldAccountId, newAccountId, result);
            return result;
        } else {
            log.error("UserAlreadyException was thrown for changeAccountId method");
            throw new UserAlreadyExistsException();
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

