package app.send.model.senders.viber.api.infobip;

import app.send.model.senders.viber.ViberSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
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
            String json = "{\"messages\":[{\"from\":\"%s\",\"to\":\"%s\",\"content\":{\"text\":\"%s\"}}]}";
            String body = String.format(json, FROM, sendTo, text);
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
            throw new InfobipException(e.getMessage());
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
            throw new InfobipException(e.getMessage());
        }
    }
}
