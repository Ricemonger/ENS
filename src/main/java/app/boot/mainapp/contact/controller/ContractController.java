package app.boot.mainapp.contact.controller;

import app.boot.authentication.security.JwtUtil;
import app.boot.mainapp.contact.model.Contact;
import app.boot.mainapp.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContractController {

    private final ContactService contactService;

    private final JwtUtil jwtUtil;
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Contact create(@RequestHeader(name="Authorization") String token, @RequestBody ContactCreUpdRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token),request.method(), request.contactId(), request.notificationName());
        return contactService.create(contact);
    }
    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Contact update(@RequestHeader(name="Authorization") String token, @RequestBody ContactCreUpdRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token),request.method(), request.contactId(), request.notificationName());
        return contactService.update(contact);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Contact delete(@RequestHeader(name="Authorization") String token, @RequestBody ContactKeyRequest request){
        Contact contact = new Contact(jwtUtil.extractUsername(token), request.method(), request.contactId());
        return contactService.delete(contact);
    }
    @GetMapping("/getByUN")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByUsername(@RequestHeader(name="Authorization") String token){
        String username = jwtUtil.extractUsername(token);
        return contactService.findAllByUsername(username);
    }
    @GetMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name="Authorization") String token, @RequestBody ContactKeyRequest pkRequest){
        String username = jwtUtil.extractUsername(token);
        String method = pkRequest.method();
        String contactId = pkRequest.contactId();
        return contactService.findAllLikePrimaryKey(username, method, contactId);
    }
    @GetMapping("/getByNN")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikeNotificationName(@RequestHeader(name="Authorization") String token, @RequestBody ContactNNRequest nnRequest){
        String username = jwtUtil.extractUsername(token);
        return contactService.findAllLikeNotificationName(username, nnRequest.notificationName());
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundException(){
        return "ERROR 404: Contact doesnt exist";
    }
    @ExceptionHandler(ContactAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String alreadyExists(){return "ERROR 403: Contact already exist";}

}
