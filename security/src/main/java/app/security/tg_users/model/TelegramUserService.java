package app.security.tg_users.model;

import app.security.abstract_users.exceptions.UserAlreadyExistsException;
import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.model.AnyUserService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {
    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final AnyUserService anyUserService;

    private final EnsUserService ensUserService;

    private final TelegramFeignClientService telegramFeignClientService;

    private final ContactFeignClientService contactFeignClientService;

    private final NotificationFeignClientService notificationFeignService;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public TelegramUser create(String telegramToken) {
        String chatId = telegramFeignClientService.getChatId(telegramToken);
        TelegramUser telegramUser = new TelegramUser(chatId);
        return createOrThrow(telegramUser);
    }

    public String generateSecurityToken(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        log.trace("SecurityToken-{} was generated for accountId-{}", securityToken, accountId);
        return securityToken;
    }

    public void link(String telegramToken, String username, String password) {
        if (ensUserService.canLogin(username, password)) {
            String oldAccountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
            String newAccountId = ensUserService.getByUsername(username).getAccountId();

            String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
            String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);

            anyUserService.delete(oldAccountId);

            TelegramUser telegramUser = new TelegramUser(newAccountId, getChatId(telegramToken));
            createOrThrow(telegramUser);

            contactFeignClientService.changeAccountId(oldAccountIdToken, newAccountIdToken);
            notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        } else {
            throw new UserDoesntExistException();
        }
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);
        String oldAccountId = inDb.getAccountId();

        deleteOrThrow(inDb);
        inDb.setAccountId(null);
        inDb = createOrThrow(inDb);

        String newAccountId = inDb.getAccountId();

        changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = getByChatIdOrThrow(chatId);
        deleteOrThrow(inDb);
    }

    private void changeNotificationsAndContactsAccountIds(String oldAccountId, String newAccountId) {
        String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
        String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);
        contactFeignClientService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
    }

    public boolean isLinked(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        return ensUserService.doesUserExist(accountId);
    }

    public String getAccountInfo(String telegramToken) {
        String chatId = getChatId(telegramToken);
        String accountId = getAccountIdByTelegramTokenOrThrow(chatId);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        List<Notification> notifications = notificationFeignService.findAllById(securityToken);
        List<Contact> contacts = contactFeignClientService.findAllById(securityToken);

        String sb = "ChatId: " + chatId + "\n" +
                "AccountId: " + accountId + "\n" +
                "List of notifications:\n" +
                notifications + "\n" +
                "List of contacts:\n" +
                contacts + "\n";

        return sb;
    }

    public void delete(String telegramToken) {
        TelegramUser toDelete = new TelegramUser(getChatId(telegramToken));
        deleteOrThrow(toDelete);
    }

    private String getAccountIdByTelegramTokenOrThrow(String telegramToken) {
        String chatId = getChatId(telegramToken);
        return getAccountIdByChatIdOrThrow(chatId);
    }

    private String getAccountIdByChatIdOrThrow(String chatId) {
        TelegramUser inDb = getByChatIdOrThrow(chatId);
        return inDb.getAccountId();
    }

    private String getChatId(String telegramToken) {
        return telegramFeignClientService.getChatId(telegramToken);
    }

    private TelegramUser createOrThrow(TelegramUser user) {
        if (!doesUserExistByChatId(user.getChatId())) {
            TelegramUser result = telegramUserRepositoryService.save(user);
            log.trace("create method was called with request-{} and result-{}", user, result);
            return result;
        } else {
            log.error("UserAlreadyExistsException was thrown during create method for user-{}", user);
            throw new UserAlreadyExistsException();
        }
    }

    private TelegramUser deleteOrThrow(TelegramUser user) {
        if (doesUserExistByChatId(user.getChatId())) {
            TelegramUser deleted = telegramUserRepositoryService.findByChatIdOrThrow(user.getChatId());
            telegramUserRepositoryService.delete(user);
            log.trace("delete method was called for user-{} with result:{}", user, deleted);
            return deleted;
        } else if (doesUserExistByAccountId(user.getAccountId())) {
            TelegramUser deleted = telegramUserRepositoryService.findByAccountIdOrThrow(user.getAccountId());
            telegramUserRepositoryService.delete(user);
            log.trace("delete method was called for user-{} with result:{}", user, deleted);
            return deleted;
        } else {
            log.error("UserDoesntExistsException was thrown during delete method for user-{}", user);
            throw new UserDoesntExistException();
        }
    }

    private TelegramUser getByChatIdOrThrow(String chatId) {
        if (doesUserExistByChatId(chatId)) {
            TelegramUser result = telegramUserRepositoryService.findByChatIdOrThrow(chatId);
            log.trace("getByChatId method was called for chatId-{} with result:{}", chatId, result);
            return result;
        } else {
            log.error("UserDoesntExistsException was thrown during getByChatId method for chatID-{}", chatId);
            throw new UserDoesntExistException();
        }
    }

    private boolean doesUserExistByChatId(String chatId) {
        boolean byChatId = telegramUserRepositoryService.existsByChatId(chatId);
        log.trace("doesUserExistByChatId method was called for user-{} with result:{}", chatId, byChatId);
        return byChatId;
    }

    private boolean doesUserExistByAccountId(String accountId) {
        boolean byChatId = telegramUserRepositoryService.existsByAccountId(accountId);
        log.trace("doesUserExistByAccountId method was called for user-{} with result:{}", accountId, byChatId);
        return byChatId;
    }
}
