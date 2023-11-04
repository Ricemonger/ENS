package app.telegram.security;

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

    public TelegramUser create(Long chatId) {
        TelegramUser newUser = new TelegramUser(String.valueOf(chatId));
        return telegramUserRepository.save(newUser);
    }

    public TelegramUser findByChatId(Long chatId) {
        return findByIdOrThrow(chatId);
    }

    public String findTelegramTokenOrGenerateAndPut(Long chatId) {
        String token = findTelegramTokenOrNull(chatId);
        if (token == null || token.isBlank()) {
            token = generateAndPutTelegramToken(chatId);
        }
        return token;
    }

    public String generateAndPutTelegramToken(Long chatId) {
        String token = telegramJwtUtil.generateToken(chatId);
        putTelegramToken(chatId, token);
        return token;
    }

    public TelegramUser putTelegramToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempTelegramToken(token);
        user.setTempTelegramTokenExpirationTime(new Date(new Date().getTime() + TELEGRAM_TOKEN_EXPIRATION_TIME));
        return telegramUserRepository.save(user);
    }

    public String findTelegramTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempTelegramToken();
    }

    public TelegramUser putSecurityToken(Long chatId, String token) {
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempSecurityToken(token);
        user.setTempSecurityTokenExpirationTime(new Date(new Date().getTime() + SECURITY_TOKEN_EXPIRATION_TIME));
        return telegramUserRepository.save(user);
    }

    public String findSecurityTokenOrNull(Long chatId) {
        TelegramUser user = findByIdOrThrow(chatId);
        return user.getTempSecurityToken();
    }

    public boolean doesUserExist(Long chatId) {
        try {
            findByIdOrThrow(chatId);
            return true;
        } catch (TelegramUserDoesntExist e) {
            return false;
        }
    }

    private TelegramUser findByIdOrThrow(Long chatId) {
        try {
            return telegramUserRepository.findById(String.valueOf(chatId)).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new TelegramUserDoesntExist();
        }
    }
}