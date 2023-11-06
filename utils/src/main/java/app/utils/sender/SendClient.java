package app.utils.sender;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification", url = "${application.config.notification.url}")
public interface SendClient {

    @PostMapping("/one")
    void sendOne(@RequestHeader(name = "Authorization") String securityToken,
                 @RequestBody SendOneRequest request);

    @PostMapping("/all")
    void sendAll(@RequestHeader(name = "Authorization") String securityToken);
}
