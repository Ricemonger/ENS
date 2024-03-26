package app.notification.controller;

import app.notification.model.Notification;
import app.notification.model.NotificationService;
import app.utils.services.notification.dto.NotificationCreUpdRequest;
import app.utils.services.notification.dto.NotificationNameRequest;
import app.utils.services.security.abstact.feign.SecurityFeignClientService;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationControllerService {

    private final NotificationService notificationService;

    private final SecurityFeignClientService jwtUtil;

    public Notification create(String securityToken, NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim(), request.text().trim());
        return notificationService.create(notification);
    }

    public Notification update(String securityToken, NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim(), request.text().trim());
        return notificationService.update(notification);
    }

    public Notification delete(String securityToken, NotificationNameRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim());
        return notificationService.delete(notification);
    }

    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        notificationService.clear(accountId);
    }

    void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        notificationService.changeAccountId(oldAccountId, newAccountId);
    }

    public List<Notification> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        return notificationService.findAllByAccountId(accountId);
    }

    public List<Notification> findAllLikePrimaryKey(String securityToken, NotificationNameRequest request) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = request.name().trim();
        return notificationService.findAllLikePrimaryKey(accountId, notificationName);
    }
}
