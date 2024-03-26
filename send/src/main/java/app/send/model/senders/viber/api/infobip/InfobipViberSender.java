package app.send.model.senders.viber.api.infobip;

import app.send.model.senders.viber.ViberSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InfobipViberSender implements ViberSender {

    private final String URL;

    private final String FROM;

    private final String AUTH_TOKEN;

    private final WebClient WEB_CLIENT;

    public InfobipViberSender(InfobipAuthConfigurer infobipConfig) {
        URL = infobipConfig.getViberUrl();
        FROM = infobipConfig.getCompanyName();
        AUTH_TOKEN = infobipConfig.getAuthToken();
        WEB_CLIENT = WebClient
                .builder()
                .baseUrl(URL)
                .build();
    }

    @Override
    public void send(String sendTo, String text) {
        try {
            String jsonFormat = "{\"messages\":[{\"from\":\"%s\",\"to\":\"%s\",\"content\":{\"text\":\"%s\"}}]}";
            String body = String.format(jsonFormat, FROM, sendTo, text);
            String response = WEB_CLIENT
                    .post()
                    .header("Authorization", "App " + AUTH_TOKEN)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
        } catch (WebClientResponseException e) {
            log.info("send throws: {}", e);
            throw new InfobipException(e.getMessage());
        }
    }

    @Override
    public void bulkSend(Map<String, List<String>> sendings) {
        String jsonFormat = "{\"messages\":[%s]}";
        String sendingJsonFormat = "{\"from\":\"%s\",\"to\":\"%s\",\"content\":{\"text\":\"%s\"}}";
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : sendings.entrySet()) {
            String text = entry.getKey();
            List<String> sendToList = entry.getValue();
            for (String sendTo : sendToList) {
                stringBuilder.append(String.format(sendingJsonFormat, FROM, sendTo, text)).append(",");
            }
        }
        String bulkMessage = stringBuilder.toString();
        if (bulkMessage.endsWith(",")) {
            bulkMessage = bulkMessage.substring(0, bulkMessage.length() - 1);
        }
        String body = String.format(jsonFormat, bulkMessage);
        System.out.println(body);
        try {
            String response = WEB_CLIENT
                    .post()
                    .header("Authorization", "App " + AUTH_TOKEN)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
        } catch (WebClientResponseException e) {
            log.info("bulkSend throws: {}", e);
            throw new InfobipException(e.getMessage());
        }
    }
}
