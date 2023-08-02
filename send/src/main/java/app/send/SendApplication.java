package app.send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendApplication.class, args);
    }

}
