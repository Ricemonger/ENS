package app.boot.notification.controller;

import app.boot.notification.controller.dto.NotificationCreUpdRequest;
import app.boot.notification.controller.dto.NotificationNameRequest;
import app.boot.notification.controller.exceptions.NotificationAlreadyExistsException;
import app.boot.notification.model.Notification;
import app.boot.notification.service.NotificationService;
import app.boot.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final NotificationService notificationService;

    private final JwtUtil jwtUtil;

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
    @GetMapping("/getByUN")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findByUsername(@RequestHeader("Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        log.trace("findByUsername method was called with param username: {}", username);
        return notificationService.findAllByUsername(jwtUtil.extractUsername(token));
    }
    @GetMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findByPrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request){
        String username = jwtUtil.extractUsername(token);
        String notificationName = request.name().trim();
        log.trace("findByPrimaryKey method was called with params: username-{}, notificationName-{}",username,notificationName);
        return notificationService.findAllByPrimaryKey(username,notificationName);
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(){
        log.warn("NoSuchElementException of NotificationController was thrown");
        return "ERROR 404: Notification doesnt exist";
    }
    @ExceptionHandler(NotificationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String alreadyExists(){
        log.warn("NotificationAlreadyExistsException of NotificationController was thrown");
        return "ERROR 403: Notification already exist";
    }
}

