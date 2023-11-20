package app.telegram.users.model;

import app.telegram.users.exceptions.InvalidTelegramTokenException;
import app.telegram.users.exceptions.TelegramUserAlreadyExistsException;
import app.telegram.users.exceptions.TelegramUserDoesntExistException;
import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.telegram.users.model.security_client.SecurityTelegramUserFeignClientService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {

    private static final long TELEGRAM_TOKEN_EXPIRATION_TIME = 55 * 60 * 1000;

    private static final long SECURITY_TOKEN_EXPIRATION_TIME = 55 * 60 * 1000;

    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final TelegramUserJwtUtil telegramUserJwtUtil;

    private final SecurityTelegramUserFeignClientService securityTelegramUserFeignClientService;

    public TelegramUser create(Long chatId) {
        if (!doesUserExist(chatId)) {
            telegramUserRepositoryService.save(new TelegramUser(String.valueOf(chatId)));
            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            String securityToken = securityTelegramUserFeignClientService.create(telegramToken);
            putSecurityToken(chatId, securityToken);
            TelegramUser inDb = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
            log.trace("Create method was executed for chatId-{}", chatId);
            return inDb;
        } else throw new TelegramUserAlreadyExistsException();
    }

    public String getChatIdByToken(String telegramToken) {
        try {
            return telegramUserJwtUtil.extractChatId(telegramToken);
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            throw new InvalidTelegramTokenException();
        }
    }

    public String getAccountInfo(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityTelegramUserFeignClientService.getAccountInfo(telegramToken);
    }

    public void removeAccount(Long chatId) {
        String telegramToken = telegramUserJwtUtil.generateToken(chatId);
        securityTelegramUserFeignClientService.delete(telegramToken);
        telegramUserRepositoryService.delete(new TelegramUser(String.valueOf(chatId)));
    }

    public void link(Long chatId, String username, String password) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.link(telegramToken, username, password);
    }

    public void unlink(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.unlink(telegramToken);
    }

    public boolean isLinked(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityTelegramUserFeignClientService.isLinked(telegramToken);
    }

    public String getAndPutSecurityToken(Long chatId) {
        String securityToken = findSecurityTokenOrNull(chatId);
        if (securityToken == null) {
            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            securityToken = securityTelegramUserFeignClientService.getSecurityToken(telegramToken);
            putSecurityToken(chatId, securityToken);
        }
        return securityToken;
    }

    public boolean doesUserExist(Long chatId) {
        try {
            findByIdOrThrow(chatId);
            return true;
        } catch (TelegramUserDoesntExistException e) {
            return false;
        }
    }

    private String findTelegramTokenOrGenerateAndPut(Long chatId) {
        String token = findTelegramTokenOrNull(chatId);
        if (token == null || token.isBlank()) {
            token = generateAndPutTelegramToken(chatId);
        }
        return token;
    }

    private String generateAndPutTelegramToken(Long chatId) {
        String token = telegramUserJwtUtil.generateToken(chatId);
        putTelegramToken(chatId, token);
        return token;
    }

    private TelegramUser putTelegramToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempTelegramToken(token);
        user.setTempTelegramTokenExpirationTime(new Date(new Date().getTime() + TELEGRAM_TOKEN_EXPIRATION_TIME));
        return telegramUserRepositoryService.save(user);
    }

    private String findTelegramTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempTelegramToken();
    }

    private TelegramUser putSecurityToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempSecurityToken(token);
        user.setTempSecurityTokenExpirationTime(new Date(new Date().getTime() + SECURITY_TOKEN_EXPIRATION_TIME));
        return telegramUserRepositoryService.save(user);
    }

    private String findSecurityTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempSecurityToken();
    }

    private TelegramUser findByIdOrThrow(Long chatId) {
        try {
            return telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        } catch (NoSuchElementException e) {
            throw new TelegramUserDoesntExistException();
        }
    }
}
