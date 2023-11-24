package app.security.tg_users.controller;

import app.security.tg_users.controller.dto.UsernamePasswordRequest;
import app.security.tg_users.model.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserControllerService {

    private final TelegramUserService telegramUserService;

    public String create(String telegramToken) {
        log.trace("create was called for telegramToken-{}", telegramToken);
        return telegramUserService.create(telegramToken);
    }

    public String getSecurityToken(String telegramToken) {
        log.trace("getSecurityToken was called for telegramToken-{}", telegramToken);
        return telegramUserService.getSecurityToken(telegramToken);
    }

    public void delete(String telegramToken) {
        log.trace("delete was called for telegramToken-{}", telegramToken);
        telegramUserService.delete(telegramToken);
    }

    public String getAccountInfo(String telegramToken) {
        log.trace("getAccountInfo was called for telegramToken-{}", telegramToken);
        return telegramUserService.getAccountInfo(telegramToken);
    }

    public void link(String telegramToken, UsernamePasswordRequest request) {
        log.trace("link was called for telegramToken-{} and request-{}", telegramToken, request);
        telegramUserService.link(telegramToken, request.username(), request.password());
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        log.trace("unlinkWithDataToTelegram was called for telegramToken-{}", telegramToken);
        telegramUserService.unlinkWithDataToTelegram(telegramToken);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        log.trace("unlinkWithDataToEns was called for telegramToken-{}", telegramToken);
        telegramUserService.unlinkWithDataToEns(telegramToken);
    }

    public boolean isLinked(String telegramToken) {
        log.trace("isLinked was called for telegramToken-{}", telegramToken);
        return telegramUserService.isLinked(telegramToken);
    }

    public boolean doesUserExists(String telegramToken) {
        log.trace("doesUserExists was called for telegramToken-{}", telegramToken);
        return telegramUserService.doesUserExists(telegramToken);
    }
}
