package app.utils.contact;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactClient contactClient;

    public Contact findOneByPrimaryKey(String token, String method, String contactId) {
        ContactPKRequest request = new ContactPKRequest(method, contactId);
        List<Contact> list = contactClient.findAllLikePrimaryKey(token, request);
        log.trace("ContactClient's method findAllLikePrimaryKey was executed with params: jwt-{}, body-{} and result:{}", token, request, list);
        Method m = Method.valueOf(method.toUpperCase(Locale.ROOT).trim());
        Contact result = list.stream().filter(l -> (l.getMethod().equals(m) && l.getContactId().equals(contactId))).findFirst().orElseThrow();
        log.trace("method findOneByPrimaryKey was executed with params: jwt-{}, body-{} and result:{}", token, request, result);
        return result;
    }

    public List<Contact> findAllById(String token) {
        List<Contact> result = contactClient.findAllByAccountId(token);
        log.trace("ContactClient's and Service's method findAllByAccountId was executed with params: jwt-{},result:{}",
                token, result);
        return result;
    }

    public void addMany(String securityToken, List<Contact> contacts) {
        for (Contact contact : contacts) {
            contactClient.create(securityToken, contact);
        }
        log.trace("contactClient's method addMany was executed with params: jwt-{} and contacts:{}",
                securityToken, contacts);
    }

    public void addOne(String securityToken, Contact contact) {
        contactClient.create(securityToken, contact);
        log.trace("contactClient's method addOne was executed with params: jwt-{} and contact:{}",
                securityToken, contact);
    }

    public void removeMany(String securityToken, List<Contact> contacts) {
        for (Contact contact : contacts) {
            contactClient.delete(securityToken, contact);
        }
        log.trace("contactClient's method removeMany was executed with params: jwt-{} and contacts:{}",
                securityToken, contacts);
    }

    public void removeOne(String securityToken, Contact contact) {
        contactClient.delete(securityToken, contact);
        log.trace("contactClient's method removeOne was executed with params: jwt-{} and contacts:{}",
                securityToken, contact);
    }

    public void removeAllById(String securityToken) {
        contactClient.clear(securityToken);
        log.trace("contactClient's method removeAllById was executed with params: jwt-{}", securityToken);
    }
}
