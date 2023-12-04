package app.utils.services.telegram.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "telegram-service", url = "${application.config.telegram.url}", configuration = TelegramFeignClientConfiguration.class)
public interface TelegramFeignClient {

    @GetMapping("/getChatId")
    String getChatId(@RequestHeader("Authorization") String telegramToken);

}
