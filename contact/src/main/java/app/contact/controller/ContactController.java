package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.exceptions.InvalidContactMethodException;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.utils.ExceptionMessage;
import app.utils.JwtRuntimeException;
import app.utils.feign_clients.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactControllerService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Contact create(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactCreUpdRequest request) {
        return service.create(securityToken, request);
    }

    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
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

    @PostMapping("/changeAccountId")
    @ResponseStatus(HttpStatus.OK)
    public void changeAccountId(@RequestHeader(name = "Authorization") String oldAccountIdToken, ChangeAccountIdRequest request) {
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
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactKeyRequest pkRequest) {
        return service.findAllLikePrimaryKey(securityToken, pkRequest);
    }

    @RequestMapping("/getByNN")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikeNotificationName(@RequestHeader(name = "Authorization") String securityToken, @RequestBody ContactNNRequest nnRequest) {
        return service.findAllLikeNotificationName(securityToken, nnRequest);
    }

    @ExceptionHandler(ContactDoesntExistException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ExceptionMessage contactDoesntExistException(ContactDoesntExistException e) {
        log.warn("ContactDoesntExistException was thrown: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.NOT_FOUND, "Contact with such method and contactId doesnt exist");
    }

    @ExceptionHandler(ContactAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage contactAlreadyExistsException(ContactAlreadyExistsException e) {
        log.warn("ContactAlreadyExistsException was thrown: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Contact with such method and contactId already exists");
    }

    @ExceptionHandler(InvalidContactMethodException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidContactMethodException(InvalidContactMethodException e) {
        log.warn("InvalidContactMethodException occurred, wrong Method name: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.BAD_REQUEST, "Wrong method name, valid method names are: " + Arrays.toString(Method.values()));
    }

    @ExceptionHandler(JwtRuntimeException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ExceptionMessage jwtRuntimeException(JwtRuntimeException e) {
        log.warn("JwtRuntimeException occurred: {}", e.getMessage());
        return new ExceptionMessage(HttpStatus.FORBIDDEN, "Invalid or expired jwt token, please get new token via users/login or users/register pages");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage unknownException(Exception e) {
        log.warn("UnknownException occurred: {}" + e.getMessage());
        e.printStackTrace();
        return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
