package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.function.Predicate;

@Service
public class JwtClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final static String DEFAULT_URL = "http://localhost:8080/api/security";

    private final static String DEFAULT_EXTRACT_USERNAME_URI = "/getUsername";

    private final String url;

    private final String uri;

    private final WebClient webClient;

    public JwtClient() {
        this(DEFAULT_URL,DEFAULT_EXTRACT_USERNAME_URI);
    }

    public JwtClient(String jwtUrl, String extractUsernameUri) {
        this.url = jwtUrl;
        this.uri = extractUsernameUri;
        webClient = WebClient
                .builder()
                .baseUrl(url)
                .defaultStatusHandler(Predicate.isEqual(HttpStatus.FORBIDDEN), response -> response.bodyToMono(String.class).map(JwtRuntimeException::new))
                .defaultStatusHandler(HttpStatusCode::isError, response -> response.bodyToMono(String.class).map(JwtRuntimeException::new))
                .build();
    }

    public String extractUsername(String token) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        log.trace("Extracting username from jwt token: {}", token);
        try {
            String username = webClient
                    .method(HttpMethod.GET)
                    .uri(uri)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (username == null || username.isBlank()) {
                log.warn("Invalid or expired JWT token:" + token);
                throw new JwtRuntimeException("Invalid or expired JWT token: " + token);
            }
            return username;
        } catch (WebClientRequestException e) {
            log.warn("Invalid or expired JWT token:" + token);
            throw new JwtRuntimeException(e);
        }
    }
}
