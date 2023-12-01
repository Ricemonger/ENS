package app.utils.feign_clients.contact;

import app.utils.feign_clients.ChangeAccountIdRequest;
import app.utils.feign_clients.contact.dto.ContactKeyRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MockContactFeignClient implements ContactFeignClient {

    private final static List<Contact> list = new ArrayList<>();

    static {
        list.add(new Contact(Method.SMS, "1111", "name"));
        list.add(new Contact(Method.SMS, "9999", "eman"));
        list.add(new Contact(Method.VIBER, "1111", "name"));
        list.add(new Contact(Method.VIBER, "9999", "eman"));
        list.add(new Contact(Method.EMAIL, "1111", "name"));
        list.add(new Contact(Method.EMAIL, "9999", "eman"));
        list.add(new Contact(Method.TELEGRAM, "1111", "name"));
        list.add(new Contact(Method.TELEGRAM, "9999", "eman"));
    }

    public final static List<Contact> MOCK_LIST = Collections.unmodifiableList(list);

    @Override
    public Contact create(String token, Contact request) {
        return request;
    }

    @Override
    public Contact update(String token, Contact request) {
        return request;
    }

    @Override
    public Contact delete(String token, Contact request) {
        return request;
    }

    @Override
    public void clear(String token) {
    }

    @Override
    public void changeAccountId(String oldAccountIdToken, ChangeAccountIdRequest request) {
    }

    @Override
    public List<Contact> findAllByAccountId(String token) {
        return MOCK_LIST;
    }

    @Override
    public List<Contact> findAllLikePrimaryKey(String token, ContactKeyRequest request) {
        Method method = Method.valueOf(request.method().trim().toUpperCase(Locale.ROOT));
        String contactId = request.contactId();
        return MOCK_LIST.stream().filter(c -> c.getMethod().equals(method) && c.getContactId().startsWith(contactId)).toList();
    }
}
