package app.security.tg_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.model.EnsUserService;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_module_client.TelegramModuleFeignClientService;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import app.utils.feign_clients.security.exceptions.UserAlreadyExistsException;
import app.utils.feign_clients.security.exceptions.UserDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {

    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final EnsUserService ensUserService;

    private final TelegramModuleFeignClientService telegramModuleFeignClientService;

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignService;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public String create(String telegramToken) {
        String chatId = telegramModuleFeignClientService.getChatId(telegramToken);
        if (!doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepositoryService.save(new TelegramUser(chatId));
            return abstractUserJwtUtil.generateToken(result.getAccountId());
        } else {
            log.info("create executed for telegramToken-{}, user already exists", telegramToken);
            throw new UserAlreadyExistsException();
        }
    }

    public String getSecurityToken(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        return abstractUserJwtUtil.generateToken(accountId);
    }

    public void delete(String telegramToken) {
        String chatId = telegramModuleFeignClientService.getChatId(telegramToken);
        TelegramUser inDB = getByChatIdOrThrow(chatId);
        telegramUserRepositoryService.delete(inDB);
    }

    public String getAccountInfo(String telegramToken) {
        String chatId = getChatId(telegramToken);
        String accountId = getByChatIdOrThrow(chatId).getAccountId();
        String username = "Not Registered";
        if (ensUserService.doesUserExist(accountId)) {
            username = ensUserService.getByAccountIdOrThrow(accountId).getUsername();
        }
        return "ChatId: " + chatId + "\n" +
                "Username: " + username + "\n";
    }

    public void link(String telegramToken, String username, String password) {
        if (ensUserService.canLogin(username, password)) {
            String oldAccountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
            String newAccountId = ensUserService.getByUsernameOrThrow(username).getAccountId();

            TelegramUser updatedAccountIdUser = new TelegramUser(newAccountId, getChatId(telegramToken));
            recreateWithNewAccountId(updatedAccountIdUser.getAccountId(), updatedAccountIdUser.getChatId());

            changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
        } else {
            log.info("link was called for telegramToken-{}, username-{}, password-{}, user doesn't exist",
                    telegramToken, username, password);
            throw new UserDoesntExistException();
        }
    }

    public boolean isLinked(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        return ensUserService.doesUserExist(accountId);
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);

        String oldAccountId = inDb.getAccountId();
        String newAccountId = recreateWithNewAccountId(null, inDb.getChatId());

        changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);

        recreateWithNewAccountId(null, inDb.getChatId());
    }

    public boolean doesUserExists(String telegramToken) {
        return doesUserExistByChatId(telegramModuleFeignClientService.getChatId(telegramToken));
    }

    private String recreateWithNewAccountId(String accountId, String chatId) {
        log.debug("recreateWithNewAccountId called for accountId-{} and chatId-{}", accountId, chatId);
        telegramUserRepositoryService.delete(new TelegramUser(chatId));
        String newAccountId = telegramUserRepositoryService.save(new TelegramUser(accountId, chatId)).getAccountId();
        log.trace("recreateWithNewAccountId executed for accountId-{} and chatId-{} with new accountId-{}", accountId,
                chatId, newAccountId);
        return newAccountId;
    }

    private void changeNotificationsAndContactsAccountIds(String oldAccountId, String newAccountId) {
        log.debug("changeNotificationsAndContactsIds called for oldAccountId-{} and newAccountId-{}", oldAccountId
                , newAccountId);
        String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
        String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);

        contactFeignClientService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        log.trace("changeNotificationsAndContactsIds executed for oldAccountId={}, oldAccountIdToken-{} | " +
                "newAccountId-{}, newAccountIdToken-{}", oldAccountId, oldAccountIdToken, newAccountId, newAccountIdToken);
    }

    private String getAccountIdByTelegramTokenOrThrow(String telegramToken) {
        log.debug("getAccountIdByTelegramToken called with telegramToken-{}", telegramToken);
        String chatId = getChatId(telegramToken);
        String accountId = getByChatIdOrThrow(chatId).getAccountId();
        log.trace("getAccountIdByTelegramToken executed for telegramToken-{} with result-{}", telegramToken, accountId);
        return accountId;
    }

    private String getChatId(String telegramToken) {
        log.debug("getChatId called for telegramToken-{}", telegramToken);
        String chatId = telegramModuleFeignClientService.getChatId(telegramToken);
        log.trace("getChatId executed for telegramToken-{} with result-{}", telegramToken, chatId);
        return chatId;
    }

    private TelegramUser getByChatIdOrThrow(String chatId) {
        log.debug("getByChatIdOrThrow called for chatId-{}", chatId);
        try {
            TelegramUser result = telegramUserRepositoryService.findByChatIdOrThrow(chatId);
            log.trace("getByChatIdOrThrow executed for chatId-{} with result-{}", chatId, result);
            return result;
        } catch (NoSuchElementException e) {
            log.info("getByChatIdOrThrow executed for chatID-{}, user doesn't exist", chatId);
            throw new UserDoesntExistException();
        }
    }

    private boolean doesUserExistByChatId(String chatId) {
        log.debug("doesUserExistByChatId called for chatId-{}", chatId);
        boolean result = telegramUserRepositoryService.existsByChatId(chatId);
        log.trace("doesUserExistByChatId executed for user-{} with result-{}", chatId, result);
        return result;
    }
}
