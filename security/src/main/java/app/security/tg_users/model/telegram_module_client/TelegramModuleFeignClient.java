package app.security.tg_users.model.telegram_module_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "telegram-service", url = "${application.config.telegram.url}")
public interface TelegramModuleFeignClient {

    @GetMapping("/getChatId")
    String getChatId(@RequestHeader("Authorization") String telegramToken);

}
