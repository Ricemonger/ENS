package app.telegram.users.model;

import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.utils.services.security.telegram.feign.SecurityTelegramUserFeignClientService;
import app.utils.services.telegram.exceptions.InvalidTelegramTokenException;
import app.utils.services.telegram.exceptions.TelegramUserAlreadyExistsException;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService {

    private static final long TELEGRAM_TOKEN_EXPIRATION_TIME = 55 * 60 * 1000;

    private static final long SECURITY_TOKEN_EXPIRATION_TIME = 55 * 60 * 1000;

    private final TelegramUserRepositoryService userRepositoryService;

    private final TelegramUserJwtUtil telegramUserJwtUtil;

    private final SecurityTelegramUserFeignClientService secFeignClient;

    public TelegramUser create(Long chatId) {
        if (!doesUserExist(chatId)) {
            userRepositoryService.save(new TelegramUser(String.valueOf(chatId)));

            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            String securityToken;

            if (!secFeignClient.doesUserExists(telegramToken)) {
                securityToken = secFeignClient.create(telegramToken);
            } else {
                securityToken = secFeignClient.getSecurityToken(telegramToken);
            }

            putSecurityToken(chatId, securityToken);

            return userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        } else {
            log.info("create was called for chatId-{}, but user already exists", chatId);
            throw new TelegramUserAlreadyExistsException();
        }
    }

    public String getChatIdByToken(String telegramToken) {
        try {
            return telegramUserJwtUtil.extractChatId(telegramToken);
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            log.info("getChatIdByToken was called for telegramToken-{}, token is invalid", telegramToken);
            throw new InvalidTelegramTokenException();
        }
    }

    public String getAccountInfo(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return secFeignClient.getAccountInfo(telegramToken);
    }

    public void removeAccount(Long chatId) {
        String telegramToken = telegramUserJwtUtil.generateToken(chatId);
        secFeignClient.delete(telegramToken);
        userRepositoryService.delete(new TelegramUser(String.valueOf(chatId)));
    }

    public void link(Long chatId, String username, String password) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        secFeignClient.link(telegramToken, username, password);
        setNullSecurityToken(chatId);
    }

    public void unlink(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        secFeignClient.unlink(telegramToken);
        setNullSecurityToken(chatId);
    }

    public boolean isLinked(Long chatId) {
        String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
        return secFeignClient.isLinked(telegramToken);
    }

    public void setBaseInputAndGroupForAllUsers() {
        List<TelegramUser> allUsers = userRepositoryService.findAll();
        for (TelegramUser user : allUsers) {
            user.setInputState(InputState.BASE);
            user.setInputGroup(InputGroup.BASE);
            userRepositoryService.save(user);
        }
    }

    public InputState getInputStateOrBase(Long chatId) {
        try {
            InputState inputState = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).getInputState();
            return inputState != null ? inputState : InputState.BASE;
        } catch (TelegramUserDoesntExistException e) {
            return InputState.BASE;
        }
    }

    public void setInputState(Long chatId, InputState inputState) {
        TelegramUser user = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setInputState(inputState);
        userRepositoryService.save(user);
    }

    public InputGroup getInputGroupOrBase(Long chatId) {
        try {
            InputGroup inputGroup = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).getInputGroup();
            return inputGroup != null ? inputGroup : InputGroup.BASE;
        } catch (TelegramUserDoesntExistException e) {
            return InputGroup.BASE;
        }
    }

    public void setInputGroup(Long chatId, InputGroup inputGroup) {
        TelegramUser user = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setInputGroup(inputGroup);
        userRepositoryService.save(user);
    }

    public void setActionConfirmFlag(Long chatId, boolean flag) {
        TelegramUser user = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setActionConfirmationFlag(flag);
        userRepositoryService.save(user);
    }

    public boolean getActionConfirmFlag(Long chatId) {
        return userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).isActionConfirmationFlag();
    }

    public void setCustomPhrase(Long chatId, String customPhrase) {
        TelegramUser user = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setCustomPhrase(customPhrase);
        userRepositoryService.save(user);
    }

    public String getCustomPhrase(Long chatId) {
        return userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).getCustomPhrase();
    }

    public boolean doesUserExist(Long chatId) {
        try {
            return doesUserExistInInnerDb(chatId) && secFeignClient.doesUserExists(findTelegramTokenOrGenerateAndPut(chatId));
        } catch (TelegramUserDoesntExistException e) {
            return false;
        }
    }

    public boolean doesUserExistInInnerDb(Long chatId) {
        try {
            findByIdOrThrow(chatId);
            return true;
        } catch (TelegramUserDoesntExistException e) {
            return false;
        }
    }

    public String findSecurityTokenOrGenerateAndPut(Long chatId) {
        String securityToken = findSecurityTokenOrNull(chatId);
        if (securityToken == null) {
            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            securityToken = secFeignClient.getSecurityToken(telegramToken);
            putSecurityToken(chatId, securityToken);
        }
        return securityToken;
    }

    private void setNullSecurityToken(Long chatId) {
        TelegramUser user = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setTempSecurityToken(null);
        user.setTempSecurityTokenExpirationTime(new Date());
        userRepositoryService.save(user);
    }

    private String findSecurityTokenOrNull(Long chatId) {
        log.debug("findSecurityTokenOrNull called with chatId-{}", chatId);
        String token;
        TelegramUser user = findByIdOrThrow(chatId);
        Date time = user.getTempSecurityTokenExpirationTime();
        if (time == null || time.before(new Date())) {
            token = null;
        } else {
            token = user.getTempSecurityToken();
        }
        log.trace("findSecurityTokenOrNull executed with chatId-{} and result-{}", chatId, token);
        return token;
    }

    private TelegramUser putSecurityToken(Long chatId, String securityToken) {
        log.debug("putSecurityToken called with chatId-{} and securityToken-{}", chatId, securityToken);
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempSecurityToken(securityToken);
        user.setTempSecurityTokenExpirationTime(new Date(new Date().getTime() + SECURITY_TOKEN_EXPIRATION_TIME));
        TelegramUser result = userRepositoryService.save(user);
        log.trace("putSecurityToken executed with chatId-{}, securityToken-{} and result-{}", chatId, securityToken,
                result);
        return result;
    }

    private String findTelegramTokenOrGenerateAndPut(Long chatId) {
        log.debug("findTelegramTokenOrGenerateAndPut called with chatId-{}", chatId);
        String token = findTelegramTokenOrNull(chatId);
        if (token == null || token.isBlank()) {
            token = generateAndPutTelegramToken(chatId);
        }
        log.trace("findTelegramTokenOrGenerateAndPut executed with chatId-{} and result-{}", chatId, token);
        return token;
    }

    private String findTelegramTokenOrNull(Long chatId) {
        log.debug("findTelegramTokenOrNull called for chatId-{}", chatId);
        String token;
        TelegramUser user = findByIdOrThrow(chatId);
        Date time = user.getTempTelegramTokenExpirationTime();
        if (time == null || time.before(new Date())) {
            token = null;
        } else {
            token = user.getTempTelegramToken();
        }
        log.trace("findTelegramTokenOrNull executed for chatId-{} and result-{}", chatId, token);
        return token;
    }

    private String generateAndPutTelegramToken(Long chatId) {
        log.debug("generateAndPutTelegramToken called for chatId-{}", chatId);
        String token = telegramUserJwtUtil.generateToken(chatId);
        putTelegramToken(chatId, token);
        log.trace("generateAndPutTelegramToken executed for chatId-{} and result-{}", chatId, token);
        return token;
    }

    private TelegramUser putTelegramToken(Long chatId, String token) {
        log.debug("putTelegramToken called for chatId-{}", chatId);
        TelegramUser user = findByIdOrThrow(chatId);
        user.setTempTelegramToken(token);
        user.setTempTelegramTokenExpirationTime(new Date(new Date().getTime() + TELEGRAM_TOKEN_EXPIRATION_TIME));
        TelegramUser result = userRepositoryService.save(user);
        log.trace("putTelegramToken executed for chatId-{} and result-{}", chatId, result);
        return result;
    }

    private TelegramUser findByIdOrThrow(Long chatId) {
        log.debug("findByIdOrThrow called with chatId-{}", chatId);
        try {
            TelegramUser result = userRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
            log.trace("findByIdOrThrow executed with chatId-{} and result-{}", chatId, result);
            return result;
        } catch (NoSuchElementException e) {
            log.info("findByIdOrThrow called with chatId-{}, user doesnt exists", chatId);
            throw new TelegramUserDoesntExistException();
        }
    }
}
