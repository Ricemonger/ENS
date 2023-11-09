package app.contact;

import app.utils.JwtClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContactServer {

    public static void main(String[] args) {
        SpringApplication.run(ContactServer.class, args);
    }

    @Bean
    public JwtClient jwtClient() {
        return new JwtClient();
    }
}
