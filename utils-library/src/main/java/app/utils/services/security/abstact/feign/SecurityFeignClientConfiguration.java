package app.utils.services.security.abstact.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityFeignClientConfiguration {
    @Bean
    public ErrorDecoder securityErrorDecoder() {
        return new SecurityFeignClientErrorDecoder();
    }
}
