package app.security.tg_users.service.database;

import app.security.tg_users.TelegramUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser create(TelegramUser chatId) {

    }

    public TelegramUser delete(TelegramUser user) {


    }

    public TelegramUser getByAccountId(String accountId) {


    }

    public TelegramUser getByChatId(String accountId) {


    }
}

