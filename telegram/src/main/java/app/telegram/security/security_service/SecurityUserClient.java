package app.telegram.security.security_service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "security-tg-user", url = "${application.config.security-tg-users.url}")
public interface SecurityUserClient {

    void create(String telegramToken);

    String getSecurityToken(String telegramToken);

    void link(String telegramToken, String username, String password);

    void unlink(String telegramToken);

    boolean isLinked(String telegramToken);

    String getAccountInfo(String telegramToken);

    void removeAccount(String telegramToken);
}
