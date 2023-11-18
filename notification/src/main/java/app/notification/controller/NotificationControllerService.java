package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.model.Notification;
import app.notification.model.NotificationService;
import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.security.SecurityFeignClientService;
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
        log.trace("create method was called with params: {}", notification);
        return notificationService.create(notification);
    }

    public Notification update(String securityToken, NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim(), request.text().trim());
        log.trace("update method was called with params: {}", notification);
        return notificationService.update(notification);
    }

    public Notification delete(String securityToken, NotificationNameRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim());
        log.trace("delete method was called with params: {}", notification);
        return notificationService.delete(notification);
    }

    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("delete method was called with params: {}", securityToken);
        notificationService.clear(accountId);
    }

    void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        notificationService.changeAccountId(oldAccountId, newAccountId);
    }

    public List<Notification> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("findByAccountId method was called with param accountId: {}", accountId);
        return notificationService.findAllByAccountId(accountId);
    }

    public List<Notification> findAllLikePrimaryKey(String securityToken, NotificationNameRequest request) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = request.name().trim();
        log.trace("findByPrimaryKey method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return notificationService.findAllLikePrimaryKey(accountId, notificationName);
    }
}
