package app.send.service.notification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final NotificationClient notificationClient;

    public Notification findOneByPrimaryKey(String token, String notificationName) {
        List<Notification> list = notificationClient.findAllByPrimaryKey(token, new NotificationNameRequest(notificationName));
        log.trace("NotificationClient's method findAllByPrimaryKey was executed with params: jwt-{}, notification's name-{} and result:{}", token, notificationName, list);
        Notification result = list.stream().filter(l -> l.getName().equals(notificationName)).findFirst().orElseThrow();
        log.trace("findOneByPrimaryKey method was executed with params: jwt-{}, notification's name-{} and result:{}", token, notificationName, result);
        return result;
    }

    public Map<String, String> getMapByAccountId(String token) {
        List<Notification> list = findAllByAccountId(token);
        Map<String, String> result = list.stream().collect(Collectors.toMap(Notification::getName, Notification::getText));
        log.trace("getMap method was executed with params: jwt-{} and result:{}", token, result);
        return result;
    }

    public List<Notification> findAllByAccountId(String token) {
        List<Notification> list = notificationClient.findAllByAccountId(token);
        log.trace("NotificationClient's method findAllByAccountId was executed with params: jwt-{} and result:{}",
                token, list);
        return list;
    }
}
