package app.telegram.users.db;

import app.telegram.users.TelegramUser;
import app.telegram.users.TelegramUserDoesntExist;
import app.telegram.users.TelegramUserJwtUtil;
import app.telegram.users.security_client.SecurityTelegramUserFeignClientService;
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

    private final TelegramUserJwtUtil telegramUserJwtUtil;

    private final SecurityTelegramUserFeignClientService securityTelegramUserFeignClientService;

    public TelegramUser create(Long chatId) {
        TelegramUser saved = telegramUserRepository.save(new TelegramUser(String.valueOf(chatId)));
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.create(telegramToken);
        return saved;
    }

    public String getChatIdByToken(String telegramToken) {
        return telegramUserJwtUtil.extractChatId(telegramToken);
    }

    public String getAccountInfo(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityTelegramUserFeignClientService.getAccountInfo(telegramToken);
    }

    public void removeAccount(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.delete(telegramToken);
        telegramUserRepository.delete(new TelegramUser(String.valueOf(chatId)));
    }

    public void unlink(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.unlink(telegramToken);
    }

    public void link(Long chatId, String username, String password) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        securityTelegramUserFeignClientService.link(telegramToken, username, password);
    }

    public boolean isLinked(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return securityTelegramUserFeignClientService.isLinked(telegramToken);
    }

    public TelegramUser findByChatId(Long chatId) {
        return findByIdOrThrow(chatId);
    }

    public String getSecurityToken(Long chatId) {
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
        String token = telegramUserJwtUtil.generateToken(chatId);
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
