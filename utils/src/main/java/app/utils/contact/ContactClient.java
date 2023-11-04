package app.utils.contact;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "contact", url = "${application.config.contact.url}")
public interface ContactClient {
    @GetMapping("/getByPK")
    List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactPKRequest request);

    @GetMapping("/getByAI")
    List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String token);
}
