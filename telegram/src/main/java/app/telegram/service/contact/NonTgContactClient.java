package app.telegram.service.contact;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "contact", url = "${application.config.contact.url}")
public interface NonTgContactClient {
    @GetMapping("/getByPK")
    List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactPKRequest request);

    @GetMapping("/getByUN")
    List<Contact> findAllByUsername(@RequestHeader(name = "Authorization") String token);
}
