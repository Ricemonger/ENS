package app.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "app.security",
        "app.utils.feign_clients.notification",
        "app.utils.feign_clients.contact"})
@EnableFeignClients(basePackages = {
        "app.security",
        "app.utils.feign_clients.notification",
        "app.utils.feign_clients.contact"})
public class SecurityServer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServer.class, args);
    }

}
