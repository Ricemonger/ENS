package app.boot.sender.service.viber.api.infobip;

import app.boot.sender.service.viber.ViberSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@PropertySource("authentication.properties")
@RequiredArgsConstructor
public class InfobipViberSender implements ViberSender {

    private static final String URL = "https://3vpxwv.api.infobip.com/viber/1/message/text";

    private static final String FROM = "DemoCompany";

    private final InfobipConfigurer infobipConfig;

    private static final WebClient webClient = WebClient
            .builder()
            .baseUrl(URL)
            .build();
    @Override
    public void send(String sendTo, String text) {
        try {
            String json = "{\"messages\":[{\"from\":\"%s\",\"to\":\"%s\",\"content\":{\"text\":\"%s\"}}]}";
            String body = String.format(json, FROM, sendTo, text);
            String response = webClient
                    .post()
                    .header("Authorization", "App " + infobipConfig.getAuthToken())
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
        }
        catch (WebClientResponseException e){
            throw  new InfobipException(e.getMessage());
        }
    }
}
