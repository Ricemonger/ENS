package app.security.tg_users.controller;

import app.security.tg_users.TelegramUser;
import app.security.tg_users.service.database.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tg-users")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramUserService telegramUserService;

    public TelegramUser create(TelegramUserChatIdRequest request) {
        TelegramUser user = new TelegramUser(request.chatId());
        return telegramUserService.create(user);
    }

    public TelegramUser delete(TelegramUserChatIdRequest request) {
        TelegramUser user = new TelegramUser(request.chatId());
        return telegramUserService.delete(user);
    }

    public TelegramUser getByAccountId(TelegramUserAccountIdRequest request) {
        String accountId = request.accountId();
        TelegramUser user = telegramUserService.getByAccountId(accountId);
        return user;
    }

    public TelegramUser getByChatId(TelegramUserChatIdRequest request) {
        String accountId = request.chatId();
        TelegramUser user = telegramUserService.getByChatId(accountId);
        return user;
    }

}
