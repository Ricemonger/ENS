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
        return telegramUserService.create(telegramToken);
    }

    public String getSecurityToken(String telegramToken) {
        return telegramUserService.getSecurityToken(telegramToken);
    }

    public void delete(String telegramToken) {
        telegramUserService.delete(telegramToken);
    }

    public String getAccountInfo(String telegramToken) {
        return telegramUserService.getAccountInfo(telegramToken);
    }

    public void link(String telegramToken, UsernamePasswordRequest request) {
        telegramUserService.link(telegramToken, request.username(), request.password());
    }

    public void unlinkWithDataToTelegram(String telegramToken) {
        telegramUserService.unlinkWithDataToTelegram(telegramToken);
    }

    public void unlinkWithDataToEns(String telegramToken) {
        telegramUserService.unlinkWithDataToEns(telegramToken);
    }

    public boolean isLinked(String telegramToken) {
        return telegramUserService.isLinked(telegramToken);
    }
}
