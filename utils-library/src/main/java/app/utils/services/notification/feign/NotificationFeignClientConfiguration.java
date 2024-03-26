package app.utils.services.notification.feign;

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
