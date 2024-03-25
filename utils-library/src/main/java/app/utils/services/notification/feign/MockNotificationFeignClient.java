package app.utils.services.notification.feign;

import app.utils.services.notification.Notification;
import app.utils.services.notification.dto.NotificationNameRequest;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockNotificationFeignClient implements NotificationFeignClient {

    private final static List<Notification> list = new ArrayList<>();

    static {
        list.add(new Notification("1", "1"));
        list.add(new Notification("2", "2"));
        list.add(new Notification("3", "3"));
        list.add(new Notification("4", "4"));
        list.add(new Notification("5", "5"));
        list.add(new Notification("6", "6"));
        list.add(new Notification("7", "7"));
        list.add(new Notification("8", "8"));
    }

    public final static List<Notification> MOCK_LIST = Collections.unmodifiableList(list);

    @Override
    public Notification create(String securityToken, Notification request) {
        return request;
    }

    @Override
    public Notification update(String securityToken, Notification request) {
        return request;
    }

    @Override
    public Notification delete(String securityToken, NotificationNameRequest request) {
        return new Notification(request.name(), securityToken);
    }

    @Override
    public void clear(String securityToken) {
    }

    @Override
    public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
    }

    @Override
    public List<Notification> findAllByAccountId(String securityToken) {
        return MOCK_LIST;
    }

    @Override
    public List<Notification> findAllLikePrimaryKey(String securityToken, NotificationNameRequest request) {
        return MOCK_LIST.stream().filter(notification -> notification.getName().startsWith(request.name())).toList();
    }
}
