package app.send.service.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public  interface Sender {

    public abstract void send(String sendTo, String text);

    default void bulkSend(Map<String, String> sendings){
        for(String sendTo : sendings.keySet()){
            send(sendTo,sendings.get(sendTo));
        }
    }
}
