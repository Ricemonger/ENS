package app.telegram.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private final SecurityUserClient securityUserClient;


    public String getAccountId(Long chatId) {


    }

    public String getSecurityToken(Long chatId) {


    }

    public void createUser(String telegramToken) {

    }

    public boolean isLinked(Long chatId) {


    }

    public void removeAccount(Long chatId) {

    }

    public String getAccountInfo(Long chatId) {


    }

    public void link(Long chatId, String username, String password) {

    }

    public void unlink(Long chatId) {

    }
}
