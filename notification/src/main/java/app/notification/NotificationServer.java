package app.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "app.notification",
        "app.utils.feign_clients.security"})
@EnableFeignClients(basePackages = {
        "app.utils.feign_clients.security"})
public class NotificationServer {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServer.class, args);
    }

}
