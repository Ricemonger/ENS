package app.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import utils.JwtClient;

@SpringBootApplication
public class ContactServer {

    public static void main(String[] args) {
        SpringApplication.run(ContactServer.class, args);
    }

    @Bean
    public JwtClient jwtClient(){
        return new JwtClient();
    }
}
