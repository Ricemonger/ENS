package app.contact.controller;

import app.contact.model.Contact;
import app.contact.model.ContactService;
import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.contact.dto.ContactCreUpdRequest;
import app.utils.feign_clients.contact.dto.ContactKeyRequest;
import app.utils.feign_clients.contact.dto.ContactNNRequest;
import app.utils.feign_clients.contact.exceptions.InvalidContactMethodException;
import app.utils.feign_clients.security.SecurityFeignClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactControllerService {

    private final ContactService contactService;

    private final SecurityFeignClientService jwtUtil;

    public Contact create(String securityToken, ContactCreUpdRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
        return contactService.create(contact);
    }

    public Contact update(String securityToken, ContactCreUpdRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId(), request.notificationName());
        return contactService.update(contact);
    }

    public Contact delete(String securityToken, ContactKeyRequest request) {
        Method method = getMethodOrThrow(request.method());
        Contact contact = new Contact(jwtUtil.extractAccountId(securityToken), method, request.contactId());
        return contactService.delete(contact);
    }

    public void clear(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        contactService.clear(accountId);
    }

    public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
        String oldAccountId = jwtUtil.extractAccountId(oldAccountIdToken);
        String newAccountId = jwtUtil.extractAccountId(request.newAccountIdToken());
        contactService.changeAccountId(oldAccountId, newAccountId);
    }

    public List<Contact> findAllByAccountId(String securityToken) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        return contactService.findAllByAccountId(accountId);
    }

    public List<Contact> findAllLikePrimaryKey(String securityToken, ContactKeyRequest pkRequest) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        Method method = getMethodOrThrow(pkRequest.method());
        String contactId = pkRequest.contactId();
        return contactService.findAllLikePrimaryKey(accountId, method, contactId);
    }

    public List<Contact> findAllLikeNotificationName(String securityToken, ContactNNRequest nnRequest) {
        String accountId = jwtUtil.extractAccountId(securityToken);
        String notificationName = nnRequest.notificationName();
        return contactService.findAllLikeNotificationName(accountId, notificationName);
    }

    private Method getMethodOrThrow(String methodName) {
        log.debug("getMethodOrThrow called for methodName-{}", methodName);
        try {
            Method method = Method.valueOf(methodName.toUpperCase(Locale.ROOT).trim());
            log.trace("getMethodOrThrow executed for methodName-{}, result-{}", methodName, method);
            return method;
        } catch (NullPointerException | IllegalArgumentException e) {
            log.info("getMethodOrThrow executed gor methodName-{}, methodName is invalid", methodName);
            throw new InvalidContactMethodException(e);
        }
    }
}
