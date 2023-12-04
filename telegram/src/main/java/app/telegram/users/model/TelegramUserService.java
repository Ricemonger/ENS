package app.telegram.users.model;

import app.telegram.users.model.db.TelegramUserRepositoryService;
import app.utils.feign_clients.security_telegram.SecurityTelegramUserFeignClientService;
import app.utils.feign_clients.telegram.exceptions.InvalidTelegramTokenException;
import app.utils.feign_clients.telegram.exceptions.TelegramUserAlreadyExistsException;
import app.utils.feign_clients.telegram.exceptions.TelegramUserDoesntExistException;
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

    private final TelegramUserRepositoryService telegramUserRepositoryService;

    private final TelegramUserJwtUtil telegramUserJwtUtil;

    private final SecurityTelegramUserFeignClientService securityTelegramUserFeignClientService;

    public TelegramUser create(Long chatId) {
        if (!doesUserExist(chatId)) {
            telegramUserRepositoryService.save(new TelegramUser(String.valueOf(chatId)));

            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            String securityToken = securityTelegramUserFeignClientService.create(telegramToken);

            putSecurityToken(chatId, securityToken);

            return telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
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

    public void setBaseInputAndGroupForAllUsers() {
        List<TelegramUser> allUsers = telegramUserRepositoryService.findAll();
        for (TelegramUser user : allUsers) {
            user.setInputState(InputState.BASE);
            user.setInputGroup(InputGroup.BASE);
            telegramUserRepositoryService.save(user);
        }
    }

    public InputState getInputStateOrBase(Long chatId) {
        try {
            InputState inputState = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).getInputState();
            return inputState != null ? inputState : InputState.BASE;
        } catch (TelegramUserDoesntExistException e) {
            return InputState.BASE;
        }
    }

    public void setInputState(Long chatId, InputState inputState) {
        TelegramUser user = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setInputState(inputState);
        telegramUserRepositoryService.save(user);
    }

    public InputGroup getInputGroupOrBase(Long chatId) {
        try {
            InputGroup inputGroup = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId)).getInputGroup();
            return inputGroup != null ? inputGroup : InputGroup.BASE;
        } catch (TelegramUserDoesntExistException e) {
            return InputGroup.BASE;
        }
    }

    public void setInputGroup(Long chatId, InputGroup inputGroup) {
        TelegramUser user = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
        user.setInputGroup(inputGroup);
        telegramUserRepositoryService.save(user);
    }

    public boolean doesUserExist(Long chatId) {
        try {
            findByIdOrThrow(chatId);
            return securityTelegramUserFeignClientService.doesUserExists(findTelegramTokenOrGenerateAndPut(chatId));
        } catch (TelegramUserDoesntExistException e) {
            return false;
        }
    }

    public String findSecurityTokenOrGenerateAndPut(Long chatId) {
        String securityToken = findSecurityTokenOrNull(chatId);
        if (securityToken == null) {
            String telegramToken = findTelegramTokenOrGenerateAndPut(chatId);
            securityToken = securityTelegramUserFeignClientService.getSecurityToken(telegramToken);
            putSecurityToken(chatId, securityToken);
        }
        return securityToken;
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
        TelegramUser result = telegramUserRepositoryService.save(user);
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
        TelegramUser result = telegramUserRepositoryService.save(user);
        log.trace("putTelegramToken executed for chatId-{} and result-{}", chatId, result);
        return result;
    }

    private TelegramUser findByIdOrThrow(Long chatId) {
        log.debug("findByIdOrThrow called with chatId-{}", chatId);
        try {
            TelegramUser result = telegramUserRepositoryService.findByChatIdOrThrow(String.valueOf(chatId));
            log.trace("findByIdOrThrow executed with chatId-{} and result-{}", chatId, result);
            return result;
        } catch (NoSuchElementException e) {
            log.info("findByIdOrThrow called with chatId-{}, user doesnt exists", chatId);
            throw new TelegramUserDoesntExistException();
        }
    }
}
