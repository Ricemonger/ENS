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

        Method m = Method.valueOf(method.toUpperCase(Locale.ROOT).trim());

        return list.stream().filter(l -> (l.getMethod().equals(m) && l.getContactId().equals(contactId))).findFirst().orElseThrow();
    }

    public List<Contact> findAllById(String securityToken) {
        return contactFeignClient.findAllByAccountId(securityToken);
    }

    public void addOne(String securityToken, Contact contact) {
        contactFeignClient.create(securityToken, contact);
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
    }

    public void removeOne(String securityToken, Contact contact) {
        contactFeignClient.delete(securityToken, contact);
    }

    public void removeAllById(String securityToken) {
        contactFeignClient.clear(securityToken);
    }

    public void changeAccountId(String oldAccountIdToken, String newAccountIdToken) {
        ChangeAccountIdRequest request = new ChangeAccountIdRequest(newAccountIdToken);
        contactFeignClient.changeAccountId(oldAccountIdToken, request);
    }
}
