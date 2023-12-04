package app.utils.feign_clients.security_telegram;

import app.utils.feign_clients.security_telegram.dto.UsernamePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityTelegramUserFeignClientService {

    private final SecurityTelegramUserFeignClient securityTelegramUserFeignClient;

    public String create(String telegramToken) {
        return securityTelegramUserFeignClient.create(telegramToken);
    }

    public void delete(String telegramToken) {
        securityTelegramUserFeignClient.delete(telegramToken);
    }

    public void link(String telegramToken, String username, String password) {
        UsernamePasswordRequest request = new UsernamePasswordRequest(username, password);
        securityTelegramUserFeignClient.link(telegramToken, request);
    }

    public void unlink(String telegramToken) {
        securityTelegramUserFeignClient.unlink(telegramToken);
    }

    public boolean isLinked(String telegramToken) {
        return securityTelegramUserFeignClient.isLinked(telegramToken);
    }

    public String getSecurityToken(String telegramToken) {
        return securityTelegramUserFeignClient.getSecurityToken(telegramToken);
    }

    public String getAccountInfo(String telegramToken) {
        return securityTelegramUserFeignClient.getAccountInfo(telegramToken);
    }

    public boolean doesUserExists(String telegramToken) {
        return securityTelegramUserFeignClient.doesUserExists(telegramToken);
    }
}
