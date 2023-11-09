package app.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.function.Predicate;

@Service
@Slf4j
public class SecurityJwtWebClient {

    private final static String DEFAULT_URL = "http://localhost:8080/api/security";

    private final static String DEFAULT_EXTRACT_ACCOUNT_ID_URI = "/getAccountId";

    private final String url;

    private final WebClient webClient;

    public SecurityJwtWebClient() {
        this(DEFAULT_URL);
    }

    public SecurityJwtWebClient(String jwtUrl) {
        this.url = jwtUrl;
        webClient = WebClient
                .builder()
                .baseUrl(url)
                .defaultStatusHandler(Predicate.isEqual(HttpStatus.FORBIDDEN), response -> response.bodyToMono(String.class).map(JwtRuntimeException::new))
                .defaultStatusHandler(HttpStatusCode::isError, response -> response.bodyToMono(String.class).map(JwtRuntimeException::new))
                .build();
    }

    public String extractAccountId(String token) {
        return extractString(token, DEFAULT_EXTRACT_ACCOUNT_ID_URI);
    }

    public String extractString(String securityToken, String uri) {
        if (!securityToken.startsWith("Bearer ")) {
            securityToken = "Bearer " + securityToken;
        }
        log.trace("Extracting accountId from jwt securityToken: {}", securityToken);
        try {
            String username = webClient
                    .method(HttpMethod.GET)
                    .uri(uri)
                    .header("Authorization", securityToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (username == null || username.isBlank()) {
                log.warn("Invalid or expired JWT securityToken:" + securityToken);
                throw new JwtRuntimeException("Invalid or expired JWT securityToken: " + securityToken);
            }
            return username;
        } catch (WebClientRequestException e) {
            log.warn("Invalid or expired JWT securityToken:" + securityToken);
            throw new JwtRuntimeException(e);
        }
    }
}
