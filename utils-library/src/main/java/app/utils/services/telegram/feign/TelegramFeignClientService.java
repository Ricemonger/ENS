package app.utils.services.telegram.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramFeignClientService {

    private final TelegramFeignClient telegramFeignClient;

    public String getChatId(String telegramToken) {
        return telegramFeignClient.getChatId(telegramToken);
    }
}
