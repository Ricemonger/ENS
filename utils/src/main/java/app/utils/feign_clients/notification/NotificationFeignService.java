package app.utils.feign_clients.notification;

import app.utils.feign_clients.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFeignService {

    private final NotificationFeignClient notificationFeignClient;

    public Notification findOneByPrimaryKey(String securityToken, String notificationName) {
        List<Notification> list = notificationFeignClient.findAllByPrimaryKey(securityToken, new NotificationNameRequest(notificationName));
        log.trace("NotificationFeignClient's method findAllByPrimaryKey was executed with params: jwt-{}, notification's name-{} and result:{}", securityToken, notificationName, list);
        Notification result = list.stream().filter(l -> l.getName().equals(notificationName)).findFirst().orElseThrow();
        log.trace("findOneByPrimaryKey method was executed with params: jwt-{}, notification's name-{} and result:{}", securityToken, notificationName, result);
        return result;
    }

    public Map<String, String> getMapByAccountId(String securityToken) {
        List<Notification> list = findAllById(securityToken);
        Map<String, String> result = list.stream().collect(Collectors.toMap(Notification::getName, Notification::getText));
        log.trace("getMap method was executed with params: jwt-{} and result:{}", securityToken, result);
        return result;
    }

    public List<Notification> findAllById(String securityToken) {
        List<Notification> list = notificationFeignClient.findAllByAccountId(securityToken);
        log.trace("NotificationFeignClient's method findAllByAccountId was executed with params: jwt-{} and result:{}",
                securityToken, list);
        return list;
    }

    public void addMany(String securityToken, List<Notification> notifications) {
        for (Notification notification : notifications) {
            notificationFeignClient.create(securityToken, notification);
        }
        log.trace("NotificationFeignClient's method addMany was executed with params: jwt-{} and notifications:{}",
                securityToken, notifications);
    }

    public void addOne(String securityToken, Notification notification) {
        notificationFeignClient.create(securityToken, notification);
        log.trace("NotificationFeignClient's method addOne was executed with params: jwt-{} and notification:{}",
                securityToken, notification);
    }

    public void removeMany(String securityToken, List<Notification> notifications) {
        for (Notification notification : notifications) {
            notificationFeignClient.delete(securityToken, new NotificationNameRequest(notification.getName()));
        }
        log.trace("NotificationFeignClient's method removeMany was executed with params: jwt-{} and notifications:{}",
                securityToken, notifications);
    }

    public void removeOne(String securityToken, Notification notification) {
        notificationFeignClient.delete(securityToken, new NotificationNameRequest(notification.getName()));
        log.trace("NotificationFeignClient's method removeOne was executed with params: jwt-{} and notifications:{}",
                securityToken, notification);
    }

    public void removeAllById(String securityToken) {
        notificationFeignClient.clear(securityToken);
        log.trace("NotificationFeignClient's method removeAllById was executed with params: jwt-{}", securityToken);
    }

    public void changeAccountId(String oldAccountIdToken, String newAccountIdToken) {
        notificationFeignClient.changeAccountId(oldAccountIdToken, new ChangeAccountIdRequest(newAccountIdToken));
        log.info("notificationClient's method changeAccountId was executed with params: oldToken-{}, newToken-{}", oldAccountIdToken, newAccountIdToken);
    }
}
