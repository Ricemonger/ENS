package app.security.tg_users.model.telegram_module_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramFeignClientService {

    private final TelegramFeignClient telegramFeignClient;

    public String getChatId(String telegramToken) {
        log.trace("getChatId method was called with token-{}", telegramToken);
        return telegramFeignClient.getChatId(telegramToken);
    }
}
