package app.boot.sender.service.viber.api.infobip;

import app.boot.sender.service.viber.ViberSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@PropertySource("application.properties")
@RequiredArgsConstructor
public class InfobipViberSender implements ViberSender {

    //@Value("${infobip.viber.url}")
    private final static String URL="https://3vpxwv.api.infobip.com/viber/1/message/text";

    //@Value("${infobip.viber.company}")
    private final static String FROM="DemoCompany";

    private final InfobipAuthConfigurer infobipConfig;

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

    @Override
    public void bulkSend(Map<String, String> sendings) {
        String json = "{\"messages\":[%s]}";
        String sendingJson = "{\"from\":\"%s\",\"to\":\"%s\",\"content\":{\"text\":\"%s\"}}";
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sendings.entrySet()) {
            stringBuilder.append(String.format(sendingJson, FROM, entry.getKey(), entry.getValue())).append(",");
        }
        String bulkMessage = stringBuilder.toString();
        if (bulkMessage.endsWith(",")) {
            bulkMessage = bulkMessage.substring(0, bulkMessage.length() - 1);
        }
        String body = String.format(json, bulkMessage);
        System.out.println(body);
        try {
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
        } catch (WebClientResponseException e) {
            throw new InfobipException(e.getMessage());
        }
    }
}
