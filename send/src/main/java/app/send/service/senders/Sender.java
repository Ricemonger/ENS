package app.send.service.senders;

import java.util.Map;

public interface Sender {

    void send(String sendTo, String text);

    default void bulkSend(Map<String, String> sendings) {
        for (String sendTo : sendings.keySet()) {
            send(sendTo, sendings.get(sendTo));
        }
    }
}
