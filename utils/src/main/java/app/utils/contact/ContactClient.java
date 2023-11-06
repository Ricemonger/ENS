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

    @DeleteMapping("/clear")
    void clear(@RequestHeader(name = "Authorization") String token);

    @DeleteMapping
    void delete(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @PostMapping
    void create(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);
}
