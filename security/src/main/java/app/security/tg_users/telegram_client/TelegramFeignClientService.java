package app.security.tg_users.telegram_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramFeignClientService {

    private final TelegramFeignClient telegramFeignClient;

    public String getChatId(String telegramToken) {
        return telegramFeignClient.getChatId(telegramToken);
    }
}
