package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.exceptions.NotificationAlreadyExistsException;
import app.notification.exceptions.NotificationDoesntExistException;
import app.notification.service.Notification;
import app.notification.service.db.NotificationRepositoryService;
import app.utils.ExceptionMessage;
import app.utils.JwtClient;
import app.utils.JwtRuntimeException;
import app.utils.feign_clients.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationRepositoryService notificationRepositoryService;

    private final JwtClient jwtUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(token), request.name().trim(), request.text().trim());
        log.trace("create method was called with params: {}", notification);
        return notificationRepositoryService.create(notification);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification update(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(token), request.name().trim(), request.text().trim());
        log.trace("update method was called with params: {}", notification);
        return notificationRepositoryService.update(notification);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public void clear(@RequestHeader("Authorization") String token) {
        String accountId = jwtUtil.extractAccountId(token);
        log.trace("delete method was called with params: {}", token);
        notificationRepositoryService.clear(accountId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification delete(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request) {
        Notification notification = new Notification(jwtUtil.extractAccountId(token), request.name().trim());
        log.trace("delete method was called with params: {}", notification);
        return notificationRepositoryService.delete(notification);
    }

    @RequestMapping("/getByUN")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllByAccountIdUN(@RequestHeader("Authorization") String token) {
        return findAllByAccountId(token);
    }

    @RequestMapping("/getByAI")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllByAccountId(@RequestHeader("Authorization") String token) {
        String accountId = jwtUtil.extractAccountId(token);
        log.trace("findByAccountId method was called with param accountId: {}", accountId);
        return notificationRepositoryService.findAllByAccountId(jwtUtil.extractAccountId(token));
    }

    @RequestMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllLikePrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request) {
        String accountId = jwtUtil.extractAccountId(token);
        String notificationName = request.name().trim();
        log.trace("findByPrimaryKey method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return notificationRepositoryService.findAllLikePrimaryKey(accountId, notificationName);
    }

    @PostMapping("/changeAccountId")
    @ResponseStatus(HttpStatus.OK)
    void changeAccountId(@RequestHeader(name = "Authorization") String oldAccountIdToken,
                         @RequestBody ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        notificationRepositoryService.changeAccountId(oldAccountId, newAccountId);
    }

    @ExceptionHandler(NotificationDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage notificationDoesntExistException(NotificationDoesntExistException e) {
        log.warn("NotificationDoesntExistException of NotificationController was thrown");
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "You dont have notification with such name");
    }

    @ExceptionHandler(NotificationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage notificationAlreadyExistsException(NotificationAlreadyExistsException e) {
        log.warn("NotificationAlreadyExistsException of NotificationController was thrown");
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Your notification with same name already exists");
    }

    @ExceptionHandler(JwtRuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage jwtRuntimeException(JwtRuntimeException e) {
        log.warn("JwtRuntimeException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}

