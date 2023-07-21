package app.boot.contact.controller;

import app.boot.contact.controller.dto.ContactKeyRequest;
import app.boot.contact.controller.dto.ContactNNRequest;
import app.boot.contact.controller.exceptions.ContactAlreadyExistsException;
import app.boot.contact.model.Contact;
import app.boot.contact.controller.dto.ContactCreUpdRequest;
import app.boot.contact.service.ContactService;
import app.boot.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContractController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactService contactService;

    private final JwtUtil jwtUtil;
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Contact create(@RequestHeader(name="Authorization") String token, @RequestBody ContactCreUpdRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token),request.method(), request.contactId(), request.notificationName());
        log.trace("create method was called with params: {}",contact);
        return contactService.create(contact);
    }
    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Contact update(@RequestHeader(name="Authorization") String token, @RequestBody ContactCreUpdRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token),request.method(), request.contactId(), request.notificationName());
        log.trace("update method was called with params: {}",contact);
        return contactService.update(contact);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Contact delete(@RequestHeader(name="Authorization") String token, @RequestBody ContactKeyRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token), request.method(), request.contactId());
        log.trace("delete method was called with params: {}",contact);
        return contactService.delete(contact);
    }
    @GetMapping("/getByUN")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByUsername(@RequestHeader(name="Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        log.trace("findAllByUsername method was called with params: username-{}",username);
        return contactService.findAllByUsername(username);
    }
    @GetMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name="Authorization") String token, @RequestBody ContactKeyRequest pkRequest){
        String username = jwtUtil.extractUsername(token);
        String method = pkRequest.method();
        String contactId = pkRequest.contactId();
        log.trace("findAllLikePrimaryKey method was called with params: username-{}, method-{}, contactID-{}",username,method,contactId);
        return contactService.findAllLikePrimaryKey(username, method, contactId);
    }
    @GetMapping("/getByNN")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikeNotificationName(@RequestHeader(name="Authorization") String token, @RequestBody ContactNNRequest nnRequest){
        String username = jwtUtil.extractUsername(token);
        String notificationName = nnRequest.notificationName();
        log.trace("findAllLikeNotificationName method was called with params: username-{}, notificationName-{}",username,notificationName);
        return contactService.findAllLikeNotificationName(username, notificationName);
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundException(){
        log.warn("NoSuchElementException of ContactController was thrown");
        return "ERROR 404: Contact doesnt exist";
    }
    @ExceptionHandler(ContactAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String alreadyExists(){
        log.warn("ContactAlreadyExistsException of ContactController was thrown");
        return "ERROR 403: Contact already exist";
    }
}
