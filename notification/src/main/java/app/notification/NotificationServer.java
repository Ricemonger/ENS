package app.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import app.utils.JwtClient;

@SpringBootApplication
public class NotificationServer {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServer.class, args);
    }
    @Bean
    public JwtClient jwtClient(){
        return new JwtClient();
    }
}
