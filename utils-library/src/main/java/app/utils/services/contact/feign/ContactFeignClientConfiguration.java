package app.utils.services.contact.feign;

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
