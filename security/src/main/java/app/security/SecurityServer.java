package app.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SecurityServer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServer.class, args);
    }

}
