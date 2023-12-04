package app.utils.feign_clients.security_abstract;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "security"
        , url = "${application.config.security-abstract.url}"
        , configuration = SecurityFeignClientConfiguration.class)
public interface SecurityFeignClient {

    @GetMapping("/getAccountId")
    String getAccountId(@RequestHeader(name = "Authorization") String securityToken);

}
