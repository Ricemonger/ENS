package app.utils.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "notification", url = "${application.config.notification.url}")
public interface NotificationClient {
    @GetMapping("/getByAI")
    List<Notification> findAllByAccountId(@RequestHeader("Authorization") String token);

    @GetMapping("/getByPK")
    List<Notification> findAllByPrimaryKey(@RequestHeader("Authorization") String token, @RequestBody NotificationNameRequest request);
}
