package app.security.tg_users.model;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.model.EnsUserService;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_module_client.TelegramFeignClientService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {

    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final EnsUserService ensUserService;

    private final TelegramFeignClientService telegramFeignClientService;

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignService;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public String create(String telegramToken) {
        String chatId = telegramFeignClientService.getChatId(telegramToken);
        if (!doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepositoryService.save(new TelegramUser(chatId));
            log.trace("create method was called with request-{} and result-{}", telegramToken, result);
            return abstractUserJwtUtil.generateToken(result.getAccountId());
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public String getSecurityToken(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        log.trace("SecurityToken-{} was generated for accountId-{}", securityToken, accountId);
        return securityToken;
    }

    public void delete(String telegramToken) {
        String chatId = telegramFeignClientService.getChatId(telegramToken);
        TelegramUser inDB = getByChatIdOrThrow(chatId);
        log.trace("delete method was called with token-{} and result-{}", telegramToken, inDB);
        telegramUserRepositoryService.delete(inDB);
    }

    public String getAccountInfo(String telegramToken) {
        String chatId = getChatId(telegramToken);
        String accountId = getAccountIdByTelegramTokenOrThrow(chatId);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        List<Notification> notifications = notificationFeignService.findAllById(securityToken);
        List<Contact> contacts = contactFeignClientService.findAllById(securityToken);
        log.trace("getAccountInfo was called with token-{}", telegramToken);

        String sb = "ChatId: " + chatId + "\n" +
                "AccountId: " + accountId + "\n" +
                "List of notifications:\n" +
                notifications + "\n" +
                "List of contacts:\n" +
                contacts + "\n";

        return sb;
    }

    public void link(String telegramToken, String username, String password) {
        if (ensUserService.canLogin(username, password)) {
            String oldAccountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
            String newAccountId = ensUserService.getByUsernameOrThrow(username).getAccountId();

            TelegramUser updatedAccountIdUser = new TelegramUser(newAccountId, getChatId(telegramToken));
            recreateWithNewAccountId(updatedAccountIdUser.getAccountId(), updatedAccountIdUser.getChatId());

            changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);

            log.trace("link method was called with request: token-{}, username-{} and accountIds: old-{}, new-{}",
                    telegramToken, username, oldAccountId, newAccountId);
        } else {
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

        log.trace("unlinkWithDataToTelegram method was called with token-{} and accountIds: old-{}, new-{}",
                telegramToken, oldAccountId, newAccountId);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);

        recreateWithNewAccountId(null, inDb.getChatId());

        log.trace("unlinkWithDataToEns method was called with token-{}", telegramToken);
    }

    public boolean doesUserExists(String telegramToken) {
        boolean result = doesUserExistByChatId(telegramFeignClientService.getChatId(telegramToken));
        log.trace("doesUserExists method was called for token-{} and with result-{}", telegramToken, result);
        return result;
    }

    private String recreateWithNewAccountId(String accountId, String chatId) {
        telegramUserRepositoryService.delete(new TelegramUser(chatId));
        return telegramUserRepositoryService.save(new TelegramUser(accountId, chatId)).getAccountId();
    }

    private void changeNotificationsAndContactsAccountIds(String oldAccountId, String newAccountId) {
        String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
        String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);
        contactFeignClientService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
    }

    private String getAccountIdByTelegramTokenOrThrow(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);
        return inDb.getAccountId();
    }

    private String getChatId(String telegramToken) {
        return telegramFeignClientService.getChatId(telegramToken);
    }

    private TelegramUser getByChatIdOrThrow(String chatId) {
        try {
            TelegramUser result = telegramUserRepositoryService.findByChatIdOrThrow(chatId);
            log.trace("getByChatId method was called for chatId-{} with result:{}", chatId, result);
            return result;
        } catch (NoSuchElementException e) {
            log.error("UserDoesntExistsException was thrown during getByChatId method for chatID-{}", chatId);
            throw new UserDoesntExistException();
        }
    }

    private boolean doesUserExistByChatId(String chatId) {
        boolean byChatId = telegramUserRepositoryService.existsByChatId(chatId);
        log.trace("doesUserExistByChatId method was called for user-{} with result:{}", chatId, byChatId);
        return byChatId;
    }
}
