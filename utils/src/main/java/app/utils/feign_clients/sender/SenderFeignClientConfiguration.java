package app.utils.feign_clients.sender;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderFeignClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new SenderFeignClientErrorDecoder();
    }
}
