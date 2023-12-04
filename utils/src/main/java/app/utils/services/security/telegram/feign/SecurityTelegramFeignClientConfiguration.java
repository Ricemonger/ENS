package app.utils.services.security.telegram.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityTelegramFeignClientConfiguration {
    @Bean
    public ErrorDecoder securityTelegramErrorDecoder() {
        return new SecurityTelegramFeignClientErrorDecoder();
    }
}
