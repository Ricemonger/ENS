package app.send.service.contact;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name="contact", url = "${application.config.contact.url}")
public interface ContactClient {
    @GetMapping("/getByPK")
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name="Authorization") String token, ContactPKRequest request);
    @GetMapping("/getByUN")
    public List<Contact> findAllByUsername(@RequestHeader(name="Authorization") String token);
}
