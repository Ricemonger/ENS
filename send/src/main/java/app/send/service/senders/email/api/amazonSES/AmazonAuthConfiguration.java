package app.send.service.senders.email.api.amazonSES;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("authentication.properties")
public class AmazonAuthConfiguration {
    @Value("${amazon.email.sender}")
    private String sentFrom;
    @Value("${amazon.smtp.host}")
    private String host;
    @Value("${amazon.smtp.username}")
    private String username;
    @Value("${amazon.smtp.password}")
    private String password;
}
