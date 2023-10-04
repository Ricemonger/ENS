package app.notification.controller;

import app.notification.controller.dto.NotificationCreUpdRequest;
import app.notification.controller.dto.NotificationNameRequest;
import app.notification.controller.exceptions.NotificationAlreadyExistsException;
import app.notification.controller.exceptions.NotificationDoesntExistException;
import app.notification.model.Notification;
import app.notification.service.NotificationService;
import app.utils.ExceptionMessage;
import app.utils.JwtClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import app.utils.JwtRuntimeException;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final NotificationService notificationService;

    private final JwtClient jwtUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token), request.name().trim(), request.text().trim());
        log.trace("create method was called with params: {}",notification);
        return notificationService.create(notification);
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification update(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token),request.name().trim(), request.text().trim());
        log.trace("update method was called with params: {}",notification);
        return notificationService.update(notification);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification delete(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token), request.name().trim());
        log.trace("delete method was called with params: {}",notification);
        return notificationService.delete(notification);
    }
    @RequestMapping("/getByUN")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllByUsername(@RequestHeader("Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        log.trace("findByUsername method was called with param username: {}", username);
        return notificationService.findAllByUsername(jwtUtil.extractUsername(token));
    }
    @RequestMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findAllLikePrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request){
        String username = jwtUtil.extractUsername(token);
        String notificationName = request.name().trim();
        log.trace("findByPrimaryKey method was called with params: username-{}, notificationName-{}",username,notificationName);
        return notificationService.findAllLikePrimaryKey(username,notificationName);
    }
    @ExceptionHandler(NotificationDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage notificationDoesntExistException(NotificationDoesntExistException e){
        log.warn("NotificationDoesntExistException of NotificationController was thrown");
        return new ExceptionMessage(HttpStatus.NOT_FOUND,"You dont have notification with such name");
    }
    @ExceptionHandler(NotificationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage notificationAlreadyExistsException(NotificationAlreadyExistsException e){
        log.warn("NotificationAlreadyExistsException of NotificationController was thrown");
        return new ExceptionMessage(HttpStatus.FORBIDDEN,"Your notification with same name already exists");
    }
    @ExceptionHandler(JwtRuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage jwtRuntimeException(JwtRuntimeException e){
        log.warn("JwtRuntimeException occurred: {}" + e.getMessage());
        return new ExceptionMessage(HttpStatus.FORBIDDEN,e);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e){
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR,e);
    }
}

