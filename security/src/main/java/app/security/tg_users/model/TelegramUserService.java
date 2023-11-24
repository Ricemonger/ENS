package app.security.tg_users.model;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.model.EnsUserService;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.model.db.TelegramUserRepositoryService;
import app.security.tg_users.model.telegram_module_client.TelegramModuleFeignClientService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClientService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignClientService;
import app.utils.feign_clients.security.exceptions.UserAlreadyExistsException;
import app.utils.feign_clients.security.exceptions.UserDoesntExistException;
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

    private final TelegramModuleFeignClientService telegramModuleFeignClientService;

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignService;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public String create(String telegramToken) {
        String chatId = telegramModuleFeignClientService.getChatId(telegramToken);
        if (!doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepositoryService.save(new TelegramUser(chatId));
            log.trace("create method was called with telegramToken-{} and result-{}", telegramToken, result);
            return abstractUserJwtUtil.generateToken(result.getAccountId());
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public String getSecurityToken(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        log.trace("SecurityToken-{} was generated for telegramToken-{}", securityToken, telegramToken);
        return securityToken;
    }

    public void delete(String telegramToken) {
        String chatId = telegramModuleFeignClientService.getChatId(telegramToken);
        TelegramUser inDB = getByChatIdOrThrow(chatId);
        log.trace("delete method was called with telegramToken-{} and result-{}", telegramToken, inDB);
        telegramUserRepositoryService.delete(inDB);
    }

    public String getAccountInfo(String telegramToken) {
        String chatId = getChatId(telegramToken);
        String accountId = getAccountIdByTelegramTokenOrThrow(chatId);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        List<Notification> notifications = notificationFeignService.findAllById(securityToken);
        List<Contact> contacts = contactFeignClientService.findAllById(securityToken);
        log.trace("getAccountInfo was called for telegramToken-{}", telegramToken);

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

            log.trace("link method was called with request: telegramToken-{}, username-{} and accountIds: old-{}, new-{}",
                    telegramToken, username, oldAccountId, newAccountId);

            changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
        } else {
            throw new UserDoesntExistException();
        }
    }

    public boolean isLinked(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        boolean result = ensUserService.doesUserExist(accountId);
        log.trace("isLinked was executed for telegramToken-{} and result-{}", telegramToken, result);
        return result;
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);

        String oldAccountId = inDb.getAccountId();
        String newAccountId = recreateWithNewAccountId(null, inDb.getChatId());

        log.trace("unlinkWithDataToTelegram method was called with telegramToken-{} and accountIds: old-{}, new-{}",
                telegramToken, oldAccountId, newAccountId);

        changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);

        recreateWithNewAccountId(null, inDb.getChatId());

        log.trace("unlinkWithDataToEns method was called with token-{}", telegramToken);
    }

    public boolean doesUserExists(String telegramToken) {
        boolean result = doesUserExistByChatId(telegramModuleFeignClientService.getChatId(telegramToken));
        log.trace("doesUserExists method was called for token-{} and with result-{}", telegramToken, result);
        return result;
    }

    private String recreateWithNewAccountId(String accountId, String chatId) {
        log.trace("recreateWithNewAccountId was called for accountId-{} and chatId-{}", accountId, chatId);
        telegramUserRepositoryService.delete(new TelegramUser(chatId));
        return telegramUserRepositoryService.save(new TelegramUser(accountId, chatId)).getAccountId();
    }

    private void changeNotificationsAndContactsAccountIds(String oldAccountId, String newAccountId) {
        String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
        String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);
        log.trace("changeNotificationsAndContactsIds is executing with tokens: old-{} ||| new-{}", oldAccountIdToken,
                newAccountIdToken);

        contactFeignClientService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
    }

    private String getAccountIdByTelegramTokenOrThrow(String telegramToken) {
        log.trace("getAccountIdByTelegramToken was called with telegramToken-{}", telegramToken);
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);
        return inDb.getAccountId();
    }

    private String getChatId(String telegramToken) {
        log.trace("getChatId was called for telegramToken-{}", telegramToken);
        return telegramModuleFeignClientService.getChatId(telegramToken);
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
