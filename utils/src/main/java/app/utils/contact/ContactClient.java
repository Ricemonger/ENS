package app.utils.contact;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "contact", url = "${application.config.contact.url}")
public interface ContactClient {

    @GetMapping("/getByPK")
    List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactPKRequest request);

    @GetMapping("/getByAI")
    List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String token);

    @PostMapping
    Contact create(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @DeleteMapping
    Contact delete(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @DeleteMapping("/clear")
    void clear(@RequestHeader(name = "Authorization") String token);
}
