package app.utils.services.security.telegram.feign;

import app.utils.services.security.telegram.dto.UsernamePasswordRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "security-tg-user", url = "${application.config.security-tg-users.url}", configuration = SecurityTelegramFeignClientConfiguration.class)
public interface SecurityTelegramUserFeignClient {

    @GetMapping
    boolean doesUserExists(@RequestHeader(name = "Authorization") String telegramToken);

    @PostMapping
    String create(@RequestHeader(name = "Authorization") String telegramToken);

    @DeleteMapping
    void delete(@RequestHeader(name = "Authorization") String telegramToken);

    @PostMapping("/link")
    void link(@RequestHeader(name = "Authorization") String telegramToken, @RequestBody UsernamePasswordRequest request);

    @DeleteMapping("/link")
    void unlink(@RequestHeader(name = "Authorization") String telegramToken);

    @GetMapping("/link")
    boolean isLinked(@RequestHeader(name = "Authorization") String telegramToken);

    @GetMapping("/getSecurityToken")
    String getSecurityToken(@RequestHeader(name = "Authorization") String telegramToken);

    @GetMapping("/getAccountInfo")
    String getAccountInfo(@RequestHeader(name = "Authorization") String telegramToken);
}
