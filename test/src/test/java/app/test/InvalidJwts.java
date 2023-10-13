package app.test;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

public class InvalidJwts {

    private final String EXPIRED_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5NzE3NDQwNCwiZXhwIjoxNjk3MTc4MDA0fQ.9iQtkhWj6SRNCajTZbTlPp6d28VMbu84TooC8oVWp8U";

    private final List<String> INVALID_JWT_LIST = new ArrayList<>();

    private final List<WebClient> INVALID_WEB_CLIENT_LIST = new ArrayList<>();

    public InvalidJwts(String baseUrl){
        INVALID_JWT_LIST.add(EXPIRED_JWT);
        INVALID_JWT_LIST.add("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5NzE3NDQwNCwiZXhwIjoxNjk3MTc4MDA0fQ.9iQtkhWj6SRNCajTZbTlPp6d28VMbu84TooC8oVWp");
        INVALID_JWT_LIST.add("eyJhbGciOiJIUzI1Ni.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5NzE3NDQwNCwiZXhwIjoxNjk3MTc4MDA0fQ.9iQtkhWj6SRNCajTZbTlPp6d28VMbu84TooC8oVWp8U");
        INVALID_JWT_LIST.add("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5NzE3NDQwNCwiZXhwIjoxNjk3MTc4MDA0.9iQtkhWj6SRNCajTZbTlPp6d28VMbu84TooC8oVWp8U");
        INVALID_JWT_LIST.add("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5NzE3NDQwNCwiZXhwIjoxNjk3MTc4MDA0fQ");
        INVALID_JWT_LIST.add("INVALID_JWT_TOKEN");
        WebClient.Builder builder = WebClient.builder().baseUrl(baseUrl);
        for(String jwt : INVALID_JWT_LIST){
            WebClient webClient = builder.defaultHeader("Authorization","Bearer " + jwt).build();
            INVALID_WEB_CLIENT_LIST.add(webClient);
        }
        WebClient webClient = builder.build();
        INVALID_WEB_CLIENT_LIST.add(webClient);
        webClient = builder.defaultHeader("Authorization",EXPIRED_JWT).build();
        INVALID_WEB_CLIENT_LIST.add(webClient);
    }

    public List<WebClient> getInvalidWebClientList(){
        return INVALID_WEB_CLIENT_LIST;
    }
}
