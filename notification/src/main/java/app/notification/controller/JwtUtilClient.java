package app.notification.controller;

import app.notification.controller.dto.JwtTokenRequest;
import app.notification.controller.exceptions.JwtRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JwtUtilClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String URL = "http://localhost:8080/api/security";

    private static final WebClient webClient = WebClient
            .builder()
            .baseUrl(URL)
            .defaultStatusHandler(HttpStatusCode::isError, response -> response.bodyToMono(String.class).map(JwtRuntimeException::new))
            .build();

    public String extractUsername(String token){
        if(!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        log.trace("Extracting username from jwt token: {}",token);
        JwtTokenRequest jwtTokenRequest = new JwtTokenRequest(token);
        return webClient
                .method(HttpMethod.GET)
                .uri("/getUsername")
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
