package app.send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import utils.JwtClient;

@SpringBootApplication
@EnableFeignClients
public class SendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendApplication.class, args);
    }
    @Bean
    public JwtClient jwtClient(){
        return new JwtClient();
    }
}
