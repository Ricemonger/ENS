package app.telegram.security.db;

import app.telegram.security.TelegramJwtUtil;
import app.telegram.security.TelegramUser;
import app.telegram.security.TelegramUserDoesntExist;
import app.telegram.security.security_service.SecurityUserService;
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

    private final TelegramUserRepository telegramUserRepository;

    private final TelegramJwtUtil telegramJwtUtil;

    private final SecurityUserService securityUserService;

    public TelegramUser create(Long chatId) {
        TelegramUser saved = telegramUserRepository.save(new TelegramUser(String.valueOf(chatId)));
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityUserService.createUser(telegramToken);
        return saved;
    }

    public String getChatIdByToken(String telegramToken) {
        return telegramJwtUtil.extractChatId(telegramToken);
    }

    public String getAccountInfo(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityUserService.getAccountInfo(telegramToken);
    }

    public void removeAccount(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityUserService.removeAccount(telegramToken);
        telegramUserRepository.delete(new TelegramUser(String.valueOf(chatId)));
    }

    public void unlink(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityUserService.unlink(telegramToken);
    }

    public void link(Long chatId, String username, String password) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityUserService.link(telegramToken, username, password);
    }

    public boolean isLinked(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityUserService.isLinked(telegramToken);
    }

    public TelegramUser findByChatId(Long chatId) {
        return findByIdOrThrow(chatId);
    }

    public String getSecurityToken(Long chatId) {
        String securityToken = findSecurityTokenOrNull(chatId);
        if (securityToken == null) {
            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            securityToken = securityUserService.getSecurityToken(telegramToken);
            putSecurityToken(chatId, securityToken);
        }
        return securityToken;
    }

    public boolean doesUserExist(Long chatId) {
        try {
            findByIdOrThrow(chatId);
            return true;
        } catch (TelegramUserDoesntExist e) {
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
        String token = telegramJwtUtil.generateToken(chatId);
        putTelegramToken(chatId, token);
        return token;
    }

    private TelegramUser putTelegramToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempTelegramToken(token);
        user.setTempTelegramTokenExpirationTime(new Date(new Date().getTime() + TELEGRAM_TOKEN_EXPIRATION_TIME));
        return telegramUserRepository.save(user);
    }

    private String findTelegramTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempTelegramToken();
    }

    private TelegramUser putSecurityToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempSecurityToken(token);
        user.setTempSecurityTokenExpirationTime(new Date(new Date().getTime() + SECURITY_TOKEN_EXPIRATION_TIME));
        return telegramUserRepository.save(user);
    }

    private String findSecurityTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempSecurityToken();
    }

    private TelegramUser findByIdOrThrow(Long chatId) {
        try {
            return telegramUserRepository.findById(String.valueOf(chatId)).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new TelegramUserDoesntExist();
        }
    }
}
