package app.send;

import app.utils.JwtClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class SendServer {

    public static void main(String[] args) {
        SpringApplication.run(SendServer.class, args);
    }

    @Bean
    public JwtClient jwtClient() {
        return new JwtClient();
    }
}
