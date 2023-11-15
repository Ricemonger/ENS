package app.send.model.senders;

import java.util.List;
import java.util.Map;

public interface Sender {

    void send(String sendTo, String text);

    default void bulkSend(Map<String, List<String>> sendings) {
        for (String text : sendings.keySet()) {
            for (String sendTo : sendings.get(text)) {
                send(sendTo, text);
            }
        }
    }
}
