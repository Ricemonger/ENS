package app.utils.feign_clients.contact;

import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.contact.dto.ContactKeyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactFeignClientService {

    private final ContactFeignClient contactFeignClient;

    public Contact findOneByPrimaryKey(String securityToken, String method, String contactId) {
        ContactKeyRequest request = new ContactKeyRequest(method, contactId);
        List<Contact> list = contactFeignClient.findAllLikePrimaryKey(securityToken, request);
        log.trace("ContactFeignClient's method findAllLikePrimaryKey was executed with params: jwt-{}, body-{} and result:{}", securityToken, request, list);
        Method m = Method.valueOf(method.toUpperCase(Locale.ROOT).trim());
        Contact result = list.stream().filter(l -> (l.getMethod().equals(m) && l.getContactId().equals(contactId))).findFirst().orElseThrow();
        log.trace("method findOneByPrimaryKey was executed with params: jwt-{}, body-{} and result:{}", securityToken, request, result);
        return result;
    }

    public List<Contact> findAllById(String securityToken) {
        List<Contact> result = contactFeignClient.findAllByAccountId(securityToken);
        log.trace("ContactFeignClient's and Service's method findAllByAccountId was executed with params: jwt-{},result:{}",
                securityToken, result);
        return result;
    }

    public void addOne(String securityToken, Contact contact) {
        contactFeignClient.create(securityToken, contact);
        log.trace("contactClient's method addOne was executed with params: jwt-{} and contact:{}",
                securityToken, contact);
    }

    public void removeMany(String securityToken, Contact filters) {
        List<Contact> allContacts = contactFeignClient.findAllByAccountId(securityToken);
        allContacts
                .stream()
                .filter(s -> (s.getMethod().equals(filters.getMethod())
                        && s.getContactId().startsWith(filters.getContactId())
                        && s.getNotificationName().startsWith(filters.getNotificationName())))
                .forEach(contact -> {
                    contactFeignClient.delete(securityToken, contact);
                });

        log.trace("contactClient's method removeMany was executed with params: jwt-{} and filters:{}",
                securityToken, filters);
    }

    public void removeOne(String securityToken, Contact contact) {
        contactFeignClient.delete(securityToken, contact);
        log.trace("contactClient's method removeOne was executed with params: jwt-{} and contacts:{}",
                securityToken, contact);
    }

    public void removeAllById(String securityToken) {
        contactFeignClient.clear(securityToken);
        log.trace("contactClient's method removeAllById was executed with params: jwt-{}", securityToken);
    }

    public void changeAccountId(String oldAccountIdToken, String newAccountIdToken) {
        ChangeAccountIdRequest request = new ChangeAccountIdRequest(newAccountIdToken);
        log.trace("changeAccountId was executed with params: oldToken-{}, request-{}", oldAccountIdToken, request);
        contactFeignClient.changeAccountId(oldAccountIdToken, request);
    }
}
