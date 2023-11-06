package app.utils.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "notification", url = "${application.config.notification.url}")
public interface NotificationClient {

    @GetMapping("/getByAI")
    List<Notification> findAllByAccountId(@RequestHeader("Authorization") String token);

    @GetMapping("/getByPK")
    List<Notification> findAllByPrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request);

    @PostMapping
    Notification create(@RequestHeader("Authorization") String token, @RequestBody Notification request);

    @DeleteMapping
    Notification delete(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request);

    @DeleteMapping("/clear")
    void clear(@RequestHeader("Authorization") String token);
}
