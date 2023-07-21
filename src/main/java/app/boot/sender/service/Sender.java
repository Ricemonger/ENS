package app.boot.sender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Sender {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public abstract void send(String sendTo, String text);

    public void sendLogged(String sendTo, String text){
        send(sendTo, text);
        log.trace("send method of Sender is executed");
    }
}
