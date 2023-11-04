package app.telegram.security;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SecurityClient {

    private final static String SECURITY_URL;

    private final static String CREATE_TELEGRAM_USER_URI;

    private final static String GET_SECURITY_TOKEN_URI;

    private final WebClient webClient = WebClient.builder().build();


    public void createTelegramUser(String telegramToken) {

    }

    public String getSecurityToken(String telegramToken) {

    }
}

