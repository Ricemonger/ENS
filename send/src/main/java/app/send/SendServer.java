package app.send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "app.send",
        "app.utils.feign_clients.notification",
        "app.utils.feign_clients.contact"})
@EnableFeignClients(basePackages = {
        "app.send",
        "app.utils.feign_clients.notification",
        "app.utils.feign_clients.contact"})
public class SendServer {

    public static void main(String[] args) {
        SpringApplication.run(SendServer.class, args);
    }
}
