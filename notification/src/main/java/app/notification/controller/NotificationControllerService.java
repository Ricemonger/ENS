package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.model.Notification;
import app.notification.model.db.NotificationRepositoryService;
import app.utils.SecurityJwtWebClient;
import app.utils.feign_clients.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationControllerService {

    private final NotificationRepositoryService notificationRepositoryService;

    private final SecurityJwtWebClient jwtUtil;

    public Notification create(String securityToken, NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim(), request.text().trim());
        log.trace("create method was called with params: {}", notification);
        return notificationRepositoryService.create(notification);
    }

    public Notification update(String securityToken, NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim(), request.text().trim());
        log.trace("update method was called with params: {}", notification);
        return notificationRepositoryService.update(notification);
    }

    public Notification delete(String securityToken, NotificationNameRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(securityToken), request.name().trim());
        log.trace("delete method was called with params: {}", notification);
        return notificationRepositoryService.delete(notification);
    }

    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("delete method was called with params: {}", securityToken);
        notificationRepositoryService.clear(accountId);
    }

    void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        notificationRepositoryService.changeAccountId(oldAccountId, newAccountId);
    }

    public List<Notification> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("findByAccountId method was called with param accountId: {}", accountId);
        return notificationRepositoryService.findAllByAccountId(jwtUtil.extractAccountId(securityToken));
    }

    public List<Notification> findAllLikePrimaryKey(String securityToken, NotificationNameRequest request) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = request.name().trim();
        log.trace("findByPrimaryKey method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return notificationRepositoryService.findAllLikePrimaryKey(accountId, notificationName);
    }
}
