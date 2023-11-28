package app.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "app.telegram",
        "app.utils"})
@EnableFeignClients(basePackages = {
        "app.telegram",
        "app.utils.feign_clients"})
public class TelegramServer {

    public static void main(String[] args) {
        SpringApplication.run(TelegramServer.class, args);
    }

}
