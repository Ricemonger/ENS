package app.contact.controller;

import app.contact.model.Contact;
import app.utils.ExceptionMessage;
import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.contact.dto.ContactCreUpdRequest;
import app.utils.feign_clients.contact.dto.ContactKeyRequest;
import app.utils.feign_clients.contact.dto.ContactNNRequest;
import app.utils.feign_clients.contact.exceptions.ContactAlreadyExistsException;
import app.utils.feign_clients.contact.exceptions.ContactDoesntExistException;
import app.utils.feign_clients.contact.exceptions.InvalidContactMethodException;
import app.utils.feign_clients.security_abstract.exceptions.InvalidSecurityTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${application.config.request-mappings.contact}")
@RequiredArgsConstructor
public class ContactController {

    private final ContactControllerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contact create(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactCreUpdRequest request) {
        return service.create(securityToken, request);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Contact update(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactCreUpdRequest request) {
        return service.update(securityToken, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Contact delete(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactKeyRequest request) {
        return service.delete(securityToken, request);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public void clear(@RequestHeader(name = "Authorization") String securityToken) {
        service.clear(securityToken);
    }

    @RequestMapping("/changeAccountId")
    @ResponseStatus(HttpStatus.OK)
    public void changeAccountId(@RequestHeader(name = "Authorization") String oldAccountIdToken,
                                @RequestBody ChangeAccountIdRequest request) {
        service.changeAccountId(oldAccountIdToken, request);
    }

    @RequestMapping("/getByUN")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByAccountIdUN(@RequestHeader(name = "Authorization") String securityToken) {
        return findAllByAccountId(securityToken);
    }

    @RequestMapping("/getByAI")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String securityToken) {
        return service.findAllByAccountId(securityToken);
    }

    @RequestMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactKeyRequest request) {
        return service.findAllLikePrimaryKey(securityToken, request);
    }

    @RequestMapping("/getByNN")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikeNotificationName(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactNNRequest request) {
        return service.findAllLikeNotificationName(securityToken, request);
    }

    @ExceptionHandler(InvalidContactMethodException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidContactMethodException(InvalidContactMethodException e) {
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Wrong method name, valid method names are: " + Arrays.toString(Method.values()));
    }

    @ExceptionHandler(InvalidSecurityTokenException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage jwtRuntimeException(InvalidSecurityTokenException e) {
        return new ExceptionMessage(HttpStatus.UNAUTHORIZED, "Invalid or expired jwt token, please get new token via /login or /register pages");
    }

    @ExceptionHandler(ContactAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage contactAlreadyExistsException(ContactAlreadyExistsException e) {
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Contact with such method and contactId already exists");
    }

    @ExceptionHandler(ContactDoesntExistException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ExceptionMessage contactDoesntExistException(ContactDoesntExistException e) {
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "Contact with such method and contactId doesnt exist");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    }
}
