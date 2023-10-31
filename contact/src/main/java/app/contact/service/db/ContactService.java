package app.contact.service.db;

import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.service.Contact;
import app.contact.service.Method;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContactRepository contactRepository;

    public Contact create(Contact contact) {
        try {
            getByKey(contact);
        } catch (ContactDoesntExistException e) {
            log.trace("create method was executed with params: {}", contact);
            return contactRepository.save(contact);
        }
        throw new ContactAlreadyExistsException();
    }

    public Contact delete(Contact contact) {
        Contact removed = getByKey(contact);
        contactRepository.delete(contact);
        log.trace("delete method was executed with params: {}", contact);
        return removed;
    }

    public Contact update(Contact contact) {
        Contact dbContact = getByKey(contact);
        dbContact.setNotificationName(contact.getNotificationName());
        Contact result = contactRepository.save(dbContact);
        log.trace("update method was executed with params: {} and result:{}", contact, result);
        return result;
    }

    public List<Contact> findAllLikeNotificationName(String accountId, String notificationName) {
        List<Contact> byAccountId = findAllByAccountId(accountId);
        List<Contact> result = byAccountId
                .stream()
                .filter(contact -> contact.getNotificationName().startsWith(notificationName))
                .toList();
        log.trace("findAllLikeNotificationName method was executed with params: accountId-{}, notificationName-{} and result:{}", accountId, notificationName, result);
        return result;
    }

    public List<Contact> findAllLikePrimaryKey(String accountId, Method method, String contactId) {
        List<Contact> byAccountId = findAllByAccountId(accountId);
        List<Contact> result = byAccountId
                .stream()
                .filter(contact -> (contact.getMethod().equals(method) && contact.getContactId().startsWith(contactId)))
                .toList();
        log.trace("findAllLikePrimaryKey method was executed with params: {} and result:{}", new Contact(accountId, method, contactId), result);
        return result;
    }

    public Contact findOneByPrimaryKey(String accountId, Method method, String contactId) {
        Contact result = getByKey(accountId, method, contactId);
        log.trace("findOneByPrimaryKey method was executed with params: {} and result:{}", new Contact(accountId, method, contactId), result);
        return result;
    }

    public List<Contact> findAllByAccountId(String accountId) {
        List<Contact> result = contactRepository.findAllByAccountId(accountId);
        log.trace("findAllByUsername method was executed with params: accountId-{} and result:{}", accountId, result);
        return result;
    }

    private Contact getByKey(Contact contact) {
        return getByKey(contact.getAccountId(), contact.getMethod(), contact.getContactId());
    }

    private Contact getByKey(String accountId, Method method, String contactId) {
        Contact result;
        try {
            result = contactRepository.findById(new ContactCompositeKey(accountId, method, contactId)).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ContactDoesntExistException(e);
        }
        return result;
    }
}