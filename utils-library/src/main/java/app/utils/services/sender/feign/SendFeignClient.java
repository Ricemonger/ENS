package app.utils.services.sender.feign;

import app.utils.services.sender.dto.SendManyRequest;
import app.utils.services.sender.dto.SendOneRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "send"
        , url = "${application.config.sender.url}"
        , configuration = SenderFeignClientConfiguration.class)
public interface SendFeignClient {

    @PostMapping("/one")
    void sendOne(@RequestHeader(name = "Authorization") String securityToken, @RequestBody SendOneRequest request);

    @PostMapping("/many")
    void sendMany(@RequestHeader(name = "Authorization") String securityToken, @RequestBody SendManyRequest request);

    @PostMapping("/all")
    void sendAll(@RequestHeader(name = "Authorization") String securityToken);
}
