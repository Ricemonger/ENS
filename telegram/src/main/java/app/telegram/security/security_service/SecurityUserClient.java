package app.telegram.security.security_service;

import app.utils.contact.Contact;
import app.utils.contact.ContactPKRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "security-user", url = "${application.config.security-users.url}")
public interface SecurityUserClient {
    @GetMapping("/getByPK")
    List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactPKRequest request);

    @GetMapping("/getByAI")
    List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String token);

    String getAccountId(String securityToken);

    String getSecurityToken(String telegramToken);

    void unlink(String telegramToken);

    void link(String telegramToken, String username, String password);

    String getAccountInfo(String telegramToken);

    void removeAccount(String telegramToken);

    boolean isLinked(String telegramToken);

    void createUser(String telegramToken);
    
}
