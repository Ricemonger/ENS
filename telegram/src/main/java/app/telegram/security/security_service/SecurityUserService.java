package app.telegram.security.security_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private final SecurityUserClient securityUserClient;

    public String getSecurityToken(String telegramToken) {
        return securityUserClient.getSecurityToken(telegramToken);
    }

    public void createUser(String telegramToken) {
        securityUserClient.createUser(telegramToken);
    }

    public boolean isLinked(String telegramToken) {
        return securityUserClient.isLinked(telegramToken);
    }

    public void removeAccount(String telegramToken) {
        securityUserClient.removeAccount(telegramToken);
    }

    public String getAccountInfo(String telegramToken) {
        return securityUserClient.getAccountInfo(telegramToken);
    }

    public void link(String telegramToken, String username, String password) {
        securityUserClient.link(telegramToken, username, password);
    }

    public void unlink(String telegramToken) {
        securityUserClient.unlink(telegramToken);
    }
}
