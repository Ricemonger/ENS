package app.telegram.bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:authentication.properties")
@Getter
public class TelegramBotAuthorizationConfiguration {

    @Value("${telegram.bot.token}")
    private String API_TOKEN;

    @Value("${telegram.bot.name}")
    private String BOT_NAME;

}
