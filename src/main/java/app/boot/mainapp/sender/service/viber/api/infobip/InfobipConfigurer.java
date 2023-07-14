package app.boot.mainapp.sender.service.viber.api.infobip;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
@PropertySource("authentication.properties")
public class InfobipConfigurer {
    @Value("${viber.infobip.token}")
    private String authToken;

}
