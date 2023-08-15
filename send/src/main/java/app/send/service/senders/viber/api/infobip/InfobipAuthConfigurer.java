package app.send.service.senders.viber.api.infobip;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("authentication.properties")
public class InfobipAuthConfigurer {
    @Value("${infobip.viber.token}")
    private String authToken;
    @Value("${infobip.viber.url}")
    private String viberUrl;
    @Value("${infobip.viber.company}")
    private String companyName;

}
