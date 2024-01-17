package app.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "app.telegram",
        "app.utils.logger",
        "app.utils.services.contact",
        "app.utils.services.notification",
        "app.utils.services.security.abstact",
        "app.utils.services.security.telegram",
        "app.utils.services.sender"})
@EnableFeignClients(basePackages = {
        "app.telegram",
        "app.utils.logger",
        "app.utils.services.contact",
        "app.utils.services.notification",
        "app.utils.services.security.abstact",
        "app.utils.services.security.telegram",
        "app.utils.services.sender"})
public class TelegramServer {

    public static void main(String[] args) {
        SpringApplication.run(TelegramServer.class, args);
    }

}
