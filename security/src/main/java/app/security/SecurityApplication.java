package app.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import utils.JwtClient;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
    @Bean
    public JwtClient jwtClient(){
        return new JwtClient();
    }
    //TODO TESTING, fields validation, DIVIDE INTO MICROSERVICES
}
