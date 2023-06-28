package app.boot.mainapp.notification.controller;

import app.boot.authentication.security.JwtUtil;
import app.boot.mainapp.notification.model.Notification;
import app.boot.mainapp.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    private final JwtUtil jwtUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token),request.name(), request.text());
        return notificationService.create(notification);
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification update(@RequestHeader("Authorization") String token, @RequestBody NotificationCreUpdRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token),request.name(), request.text());
        return notificationService.update(notification);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Notification delete(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request){
        Notification notification = new Notification(jwtUtil.extractUsername(token), request.name());
        return notificationService.delete(notification);
    }
    @GetMapping("/getByUN")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findByUsername(@RequestHeader("Authorization") String token){
        return notificationService.findAllByUsername(jwtUtil.extractUsername(token));
    }
    @GetMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findByPrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request){
        return notificationService.findAllByPrimaryKey(jwtUtil.extractUsername(token), request.name());
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(){return "ERROR 404: Notification doesnt exist";}
    @ExceptionHandler(NotificationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String alreadyExists(){return "ERROR 403: Notification already exist";}
}
//TODO поискать аннтоацию на замену @RequestBody для одного аргумента, чтобы не создавать лишние рекорды

