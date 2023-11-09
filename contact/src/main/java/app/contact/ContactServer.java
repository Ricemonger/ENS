package app.contact;

import app.utils.SecurityJwtWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContactServer {

    public static void main(String[] args) {
        SpringApplication.run(ContactServer.class, args);
    }

    @Bean
    public SecurityJwtWebClient jwtClient() {
        return new SecurityJwtWebClient();
    }
}
