package app.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "app.security",
        "app.utils.services.notification",
        "app.utils.services.contact",
        "app.utils.services.telegram",
        "app.utils.logger"})
@EnableFeignClients(basePackages = {
        "app.security",
        "app.utils.services.notification",
        "app.utils.services.contact",
        "app.utils.services.telegram"})
public class SecurityServer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServer.class, args);
    }

}
