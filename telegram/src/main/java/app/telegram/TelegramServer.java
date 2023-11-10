package app.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TelegramServer {

    public static void main(String[] args) {
        SpringApplication.run(TelegramServer.class, args);
    }

}
