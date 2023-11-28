package app.notification.controller;

import app.notification.model.Notification;
import app.utils.ExceptionMessage;
import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.notification.dto.NotificationCreUpdRequest;
import app.utils.feign_clients.notification.dto.NotificationNameRequest;
import app.utils.feign_clients.notification.exceptions.NotificationAlreadyExistsException;
import app.utils.feign_clients.notification.exceptions.NotificationDoesntExistException;
import app.utils.feign_clients.security.exceptions.JwtRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.config.request-mappings.notification}")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationControllerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationCreUpdRequest request) {
        return service.create(securityToken, request);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification update(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationCreUpdRequest request) {
        return service.update(securityToken, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification delete(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationNameRequest request) {
        return service.delete(securityToken, request);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public void clear(@RequestHeader("Authorization") String securityToken) {
        service.clear(securityToken);
    }

    @PostMapping("/changeAccountId")
    @ResponseStatus(HttpStatus.OK)
    void changeAccountId(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ChangeAccountIdRequest request) {
        service.changeAccountId(securityToken, request);
    }

    @RequestMapping("/getByUN")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllByAccountIdUN(@RequestHeader("Authorization") String securityToken) {
        return findAllByAccountId(securityToken);
    }

    @RequestMapping("/getByAI")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllByAccountId(@RequestHeader("Authorization") String securityToken) {
        return service.findAllByAccountId(securityToken);
    }

    @RequestMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllLikePrimaryKey(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationNameRequest request) {
        return service.findAllLikePrimaryKey(securityToken, request);
    }

    @ExceptionHandler(NotificationDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage notificationDoesntExistException(NotificationDoesntExistException e) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "You dont have notification with such name");
    }

    @ExceptionHandler(NotificationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage notificationAlreadyExistsException(NotificationAlreadyExistsException e) {
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Your notification with same name already exists");
    }

    @ExceptionHandler(JwtRuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage jwtRuntimeException(JwtRuntimeException e) {
        return new ExceptionMessage(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN EXCEPTION");
    }
}

