package app.utils.services.telegram.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramFeignClientConfiguration {
    @Bean
    public ErrorDecoder telegramErrorDecoder() {
        return new TelegramFeignClientErrorDecoder();
    }
}
