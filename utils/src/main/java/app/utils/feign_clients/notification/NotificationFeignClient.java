package app.utils.feign_clients.notification;

import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.notification.dto.NotificationNameRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "notification", url = "${application.config.notification.url}")
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
    List<Notification> findAllByPrimaryKey(@RequestHeader("Authorization") String securityToken, @RequestBody NotificationNameRequest request);

}
