package app.telegram.service.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "notification", url = "${application.config.notification.url}")
public interface NonTgNotificationClient {
    @GetMapping("/getByUN")
    List<Notification> findAllByUsername();

    @GetMapping("/getByPK")
    List<Notification> findAllByPrimaryKey();
}
