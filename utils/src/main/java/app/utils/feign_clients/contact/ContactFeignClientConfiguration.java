package app.utils.feign_clients.contact;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactFeignClientConfiguration {
    @Bean
    public ErrorDecoder contactErrorDecoder() {
        return new ContactFeignClientErrorDecoder();
    }
}
