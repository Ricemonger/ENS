package app.security.tg_users.model.telegram_module_client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramModuleFeignClientService {

    private final TelegramModuleFeignClient telegramModuleFeignClient;

    public String getChatId(String telegramToken) {
        return telegramModuleFeignClient.getChatId(telegramToken);
    }
}
