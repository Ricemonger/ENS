package app.security.tg_users.controller;

import app.security.abstract_users.exceptions.UserDoesntExistException;
import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.any_users.db.AnyUserRepositoryService;
import app.security.ens_users.db.EnsUserRepositoryService;
import app.security.tg_users.TelegramUser;
import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import app.security.tg_users.db.TelegramUserRepositoryService;
import app.security.tg_users.telegram_client.TelegramFeignClientService;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignService;
import app.utils.feign_clients.notification.Notification;
import app.utils.feign_clients.notification.NotificationFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserControllerService {

    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final TelegramFeignClientService telegramFeignClientService;

    private final AnyUserRepositoryService anyUserRepositoryService;

    private final EnsUserRepositoryService ensUserRepositoryService;

    private final ContactFeignService contactFeignService;

    private final NotificationFeignService notificationFeignService;

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public TelegramUser create(String telegramToken) {
        String chatId = telegramFeignClientService.getChatId(telegramToken);
        TelegramUser telegramUser = new TelegramUser(chatId);
        return telegramUserRepositoryService.createOrThrow(telegramUser);
    }

    public String generateSecurityToken(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        log.trace("SecurityToken-{} was generated for accountId-{}", securityToken, accountId);
        return securityToken;
    }

    public void link(String telegramToken, UsernamePasswordRequest request) {
        link(telegramToken, request.username(), request.password());
    }

    private void link(String telegramToken, String username, String password) {
        if (ensUserRepositoryService.canLogin(username, password)) {
            String oldAccountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
            String newAccountId = ensUserRepositoryService.getByUsername(username).getAccountId();

            String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
            String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);

            anyUserRepositoryService.delete(oldAccountId);

            TelegramUser telegramUser = new TelegramUser(newAccountId, getChatId(telegramToken));
            telegramUserRepositoryService.createOrThrow(telegramUser);

            contactFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
            notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        } else {
            throw new UserDoesntExistException();
        }
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = telegramUserRepositoryService.getByChatIdOrThrow(chatId);
        String oldAccountId = inDb.getAccountId();

        telegramUserRepositoryService.deleteOrThrow(inDb);
        inDb.setAccountId(null);
        inDb = telegramUserRepositoryService.createOrThrow(inDb);

        String newAccountId = inDb.getAccountId();

        changeNotificationsAndContactsAccountIds(oldAccountId, newAccountId);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        String chatId = getChatId(telegramToken);
        TelegramUser inDb = telegramUserRepositoryService.getByChatIdOrThrow(chatId);
        telegramUserRepositoryService.deleteOrThrow(inDb);
    }

    private void changeNotificationsAndContactsAccountIds(String oldAccountId, String newAccountId) {
        String oldAccountIdToken = abstractUserJwtUtil.generateToken(oldAccountId);
        String newAccountIdToken = abstractUserJwtUtil.generateToken(newAccountId);
        contactFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
        notificationFeignService.changeAccountId(oldAccountIdToken, newAccountIdToken);
    }

    public boolean isLinked(String telegramToken) {
        String accountId = getAccountIdByTelegramTokenOrThrow(telegramToken);
        return ensUserRepositoryService.doesUserExist(accountId);
    }

    public String getAccountInfo(String telegramToken) {
        String chatId = getChatId(telegramToken);
        String accountId = getAccountIdByTelegramTokenOrThrow(chatId);
        String securityToken = abstractUserJwtUtil.generateToken(accountId);
        List<Notification> notifications = notificationFeignService.findAllById(securityToken);
        List<Contact> contacts = contactFeignService.findAllById(securityToken);

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
        telegramUserRepositoryService.deleteOrThrow(toDelete);
    }

    private String getAccountIdByTelegramTokenOrThrow(String telegramToken) {
        String chatId = getChatId(telegramToken);
        return getAccountIdByChatIdOrThrow(chatId);
    }

    private String getAccountIdByChatIdOrThrow(String chatId) {
        TelegramUser inDb = telegramUserRepositoryService.getByChatIdOrThrow(chatId);
        return inDb.getAccountId();
    }

    private String getChatId(String telegramToken) {
        return telegramFeignClientService.getChatId(telegramToken);
    }
}
