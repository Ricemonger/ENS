package app.utils.feign_clients.notification;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationFeignClientConfiguration {
    @Bean
    public ErrorDecoder notificationErrorDecoder() {
        return new NotificationFeignClientErrorDecoder();
    }
}
