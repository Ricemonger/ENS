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

    public List<Contact> findAllByAccountId(String token) {
        List<Contact> result = contactClient.findAllByAccountId(token);
        log.trace("ContactClient's and Service's method findAllByAccountId was executed with params: jwt-{},result:{}",
                token, result);
        return result;
    }
}