package app.utils.services.notification.feign;

import app.utils.services.notification.Notification;
import app.utils.services.notification.dto.NotificationNameRequest;
import app.utils.services.telegram.dto.ChangeAccountIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "notification"
        , url = "${application.config.notification.url}"
        , configuration = NotificationFeignClientConfiguration.class)
public interface NotificationFeignClient {

    @PostMapping
    Notification create(@RequestHeader("Authorization") String securityToken, @RequestBody Notification request);

    @PatchMapping
    Notification update(@RequestHeader(name = "Authorization") String securityToken, @RequestBody Notification request);

    @DeleteMapping
    Notification delete(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationNameRequest request);

    @DeleteMapping("/clear")
    void clear(@RequestHeader("Authorization") String securityToken);

    @PostMapping("/changeAccountId")
    void changeAccountId(@RequestHeader(name = "Authorization") String oldAccountIdToken, @RequestBody ChangeAccountIdRequest request);

    @GetMapping("/getByAI")
    List<Notification> findAllByAccountId(@RequestHeader("Authorization") String securityToken);

    @GetMapping("/getByPK")
    List<Notification> findAllLikePrimaryKey(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationNameRequest request);

}
