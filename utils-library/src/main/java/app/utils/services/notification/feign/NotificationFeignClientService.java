package app.utils.services.notification.feign;

import app.utils.services.notification.Notification;
import app.utils.services.notification.dto.NotificationNameRequest;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFeignClientService {

    private final NotificationFeignClient notificationFeignClient;

    public Notification findOneByPrimaryKey(String securityToken, String notificationName) {
        List<Notification> list = notificationFeignClient.findAllLikePrimaryKey(securityToken, new NotificationNameRequest(notificationName));
        return list.stream().filter(l -> l.getName().equals(notificationName)).findFirst().orElseThrow();
    }

    public Map<String, String> getMapByAccountId(String securityToken) {
        List<Notification> list = findAllById(securityToken);
        Map<String, String> result = list.stream().collect(Collectors.toMap(Notification::getName, Notification::getText));
        return result;
    }

    public List<Notification> findAllById(String securityToken) {
        return notificationFeignClient.findAllByAccountId(securityToken);
    }

    public void addOne(String securityToken, Notification notification) {
        notificationFeignClient.create(securityToken, notification);
    }

    public void removeMany(String securityToken, Notification filters) {

        List<Notification> allNotifications = notificationFeignClient.findAllByAccountId(securityToken);
        allNotifications
                .stream()
                .filter(s -> (s.getName().startsWith(filters.getName())
                        && s.getText().startsWith(filters.getText())))
                .forEach(notification -> {
                    notificationFeignClient.delete(securityToken, new NotificationNameRequest(notification.getName()));
                });
    }

    public void removeOne(String securityToken, Notification notification) {
        notificationFeignClient.delete(securityToken, new NotificationNameRequest(notification.getName()));
    }

    public void removeAllById(String securityToken) {
        notificationFeignClient.clear(securityToken);
    }

    public void changeAccountId(String oldAccountIdToken, String newAccountIdToken) {
        notificationFeignClient.changeAccountId(oldAccountIdToken, new ChangeAccountIdRequest(newAccountIdToken));
    }
}
