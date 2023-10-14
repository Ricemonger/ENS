package app.notification;

import app.utils.JwtClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotificationServer {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServer.class, args);
    }

    @Bean
    public JwtClient jwtClient() {
        return new JwtClient();
    }
}
