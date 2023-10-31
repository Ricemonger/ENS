package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.exceptions.InvalidContactMethodException;
import app.contact.service.Contact;
import app.contact.service.Method;
import app.contact.service.db.ContactService;
import app.utils.ExceptionMessage;
import app.utils.JwtClient;
import app.utils.JwtRuntimeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactService contactService;

    private final JwtClient jwtUtil;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Contact create(@RequestHeader(name = "Authorization") String token, @RequestBody ContactCreUpdRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            Contact contact = new Contact(jwtUtil.extractAccountId(token), method, request.contactId(), request.notificationName());
            log.trace("create method was called with params: {}", contact);
            return contactService.create(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Contact update(@RequestHeader(name = "Authorization") String token, @RequestBody ContactCreUpdRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            log.trace("update method was called with params: {}", request);
            Contact contact = new Contact(jwtUtil.extractAccountId(token), method, request.contactId(), request.notificationName());
            log.trace("update method was called with params: {}", contact);
            return contactService.update(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Contact delete(@RequestHeader(name = "Authorization") String token, @RequestBody ContactKeyRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            Contact contact = new Contact(jwtUtil.extractAccountId(token), method, request.contactId());
            log.trace("delete method was called with params: {}", contact);
            return contactService.delete(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    @RequestMapping("/getByUN")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByAccountIdUN(@RequestHeader(name = "Authorization") String token) {
        return findAllByAccountId(token);
    }

    @RequestMapping("/getByAI")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Contact> findAllByAccountId(@RequestHeader(name = "Authorization") String token) {
        String accountId = jwtUtil.extractAccountId(token);
        log.trace("findAllByUsername method was called with params: accountId-{}", accountId);
        return contactService.findAllByAccountId(accountId);
    }

    @RequestMapping("/getByPK")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikePrimaryKey(@RequestHeader(name = "Authorization") String token, @RequestBody ContactKeyRequest pkRequest) {
        try {
            String accountId = jwtUtil.extractAccountId(token);
            Method method = Method.valueOf(pkRequest.method().toUpperCase(Locale.ROOT).trim());
            String contactId = pkRequest.contactId();
            log.trace("findAllLikePrimaryKey method was called with params: accountId-{}, method-{}, contactID-{}", accountId, method, contactId);
            return contactService.findAllLikePrimaryKey(accountId, method, contactId);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    @RequestMapping("/getByNN")
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> findAllLikeNotificationName(@RequestHeader(name = "Authorization") String token, @RequestBody ContactNNRequest nnRequest) {
        String accountId = jwtUtil.extractAccountId(token);
        String notificationName = nnRequest.notificationName();
        log.trace("findAllLikeNotificationName method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return contactService.findAllLikeNotificationName(accountId, notificationName);
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
