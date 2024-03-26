package app.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "app.contact",
        "app.utils.services.security.abstact",
        "app.utils.logger"})
@EnableFeignClients(basePackages = {
        "app.utils.services.security.abstact.feign"})
public class ContactServer {

    public static void main(String[] args) {
        SpringApplication.run(ContactServer.class, args);
    }

}
