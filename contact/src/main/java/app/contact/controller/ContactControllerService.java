package app.contact.controller;

import app.contact.controller.dto.ContactCreUpdRequest;
import app.contact.controller.dto.ContactKeyRequest;
import app.contact.controller.dto.ContactNNRequest;
import app.contact.exceptions.InvalidContactMethodException;
import app.contact.model.Contact;
import app.contact.model.Method;
import app.contact.model.db.ContactRepositoryService;
import app.utils.SecurityJwtWebClient;
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

    private final SecurityJwtWebClient jwtUtil;

    public Contact create(String securityToken, ContactCreUpdRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
        log.trace("create method was called with params: {}", contact);
        return contactRepositoryService.create(contact);
    }

    public Contact update(String securityToken, ContactCreUpdRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
        log.trace("update method was called with params: {}", contact);
        return contactRepositoryService.update(contact);
    }

    public Contact delete(String securityToken, ContactKeyRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId());
        log.trace("delete method was called with params: {}", contact);
        return contactRepositoryService.delete(contact);
    }

    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("clear method was called with jwt: {}", securityToken);
        contactRepositoryService.clear(accountId);
    }

    public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        contactRepositoryService.changeAccountId(oldAccountId, newAccountId);
    }

    public List<Contact> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        log.trace("findAllByAccountId method was called with params: accountId-{}", accountId);
        return contactRepositoryService.findAllByAccountId(accountId);
    }

    public List<Contact> findAllLikePrimaryKey(String securityToken, ContactKeyRequest pkRequest) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        Method method = getMethodOrThrow(pkRequest.method());
        String contactId = pkRequest.contactId();
        log.trace("findAllLikePrimaryKey method was called with params: accountId-{}, method-{}, contactID-{}", accountId, method, contactId);
        return contactRepositoryService.findAllLikePrimaryKey(accountId, method, contactId);
    }

    public List<Contact> findAllLikeNotificationName(String securityToken, ContactNNRequest nnRequest) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = nnRequest.notificationName();
        log.trace("findAllLikeNotificationName method was called with params: accountId-{}, notificationName-{}", accountId, notificationName);
        return contactRepositoryService.findAllLikeNotificationName(accountId, notificationName);
    }

    private Method getMethodOrThrow(String methodName) {
        try {
            return Method.valueOf(methodName.toUpperCase(Locale.ROOT).trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidContactMethodException(e);
        }
    }
}