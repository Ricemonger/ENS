package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.InvalidContactMethodException;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.model.db.ContactRepositoryService;
import app.utils.JwtClient;
import app.utils.feign_clients.ChangeAccountIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactControllerService {

    private final ContactRepositoryService contactRepositoryService;

    private final JwtClient jwtUtil;

    public Contact create(String securityToken, ContactCreUpdRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
            log.trace("create method was called with params: {}", contact);
            return contactRepositoryService.create(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    public Contact update(String securityToken, ContactCreUpdRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            log.trace("update method was called with params: {}", request);
            Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
            log.trace("update method was called with params: {}", contact);
            return contactRepositoryService.update(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }


    public Contact delete(String securityToken, ContactKeyRequest request) {
        try {
            Method method = Method.valueOf(request.method().toUpperCase(Locale.ROOT).trim());
            Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId());
            log.trace("delete method was called with params: {}", contact);
            return contactRepositoryService.delete(contact);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }


    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("clear method was called with jwt: {}", securityToken);
        contactRepositoryService.clear(accountId);
    }

    public List<Contact> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("findAllByAccountId method was called with params: accountId-{}", accountId);
        return contactRepositoryService.findAllByAccountId(accountId);
    }

    public List<Contact> findAllLikePrimaryKey(String securityToken, ContactKeyRequest pkRequest) {
        try {
            String accountId = jwtUtil.extractAccountId(securityToken);
            Method method = Method.valueOf(pkRequest.method().toUpperCase(Locale.ROOT).trim());
            String contactId = pkRequest.contactId();
            log.trace("findAllLikePrimaryKey method was called with params: accountId-{}, method-{}, contactID-{}", accountId, method, contactId);
            return contactRepositoryService.findAllLikePrimaryKey(accountId, method, contactId);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }

    public List<Contact> findAllLikeNotificationName(String securityToken, ContactNNRequest nnRequest) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = nnRequest.notificationName();
        log.trace("findAllLikeNotificationName method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return contactRepositoryService.findAllLikeNotificationName(accountId, notificationName);
    }


    public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        contactRepositoryService.changeAccountId(oldAccountId, newAccountId);
    }
}
