package app.utils.feign_clients.contact;

import app.utils.feign_clients.ChangeAccountIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "contact", url = "${application.config.contact.url}")
public interface ContactFeignClient {

    @PostMapping
    Contact create(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @PatchMapping
    Contact update(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @DeleteMapping
    Contact delete(@RequestHeader(name = "Authorization") String token, @RequestBody Contact request);

    @DeleteMapping("/clear")
    void clear(@RequestHeader(name = "Authorization") String token);

    @PostMapping("/changeAccountId")
    void changeAccountId(@RequestHeader(name = "Authorization") String oldAccountIdToken, @RequestBody ChangeAccountIdRequest request);

    @GetMapping("/getByAI")
    List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String token);

    @GetMapping("/getByPK")
    List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactPKRequest request);
}
