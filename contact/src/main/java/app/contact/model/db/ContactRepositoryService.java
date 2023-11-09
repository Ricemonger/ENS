package app.contact.model.db;

import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.model.Contact;
import app.contact.model.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ContactRepositoryService {

    private final ContactRepository contactRepository;

    public Contact create(Contact contact) {
        try {
            getByKeyOrThrow(contact);
        } catch (ContactDoesntExistException e) {
            log.trace("create method was executed with params: {}", contact);
            return contactRepository.save(contact);
        }
        throw new ContactAlreadyExistsException();
    }

    public Contact delete(Contact contact) {
        Contact removed = getByKeyOrThrow(contact);
        contactRepository.delete(contact);
        log.trace("delete method was executed with params: {}", contact);
        return removed;
    }

    public void clear(String accountId) {
        List<Contact> toDelete = contactRepository.findAllByAccountId(accountId);
        log.trace("clear method was called with accountId: {}", accountId);
        contactRepository.deleteAll(toDelete);
    }

    public Contact update(Contact contact) {
        Contact dbContact = getByKeyOrThrow(contact);
        dbContact.setNotificationName(contact.getNotificationName());
        Contact result = contactRepository.save(dbContact);
        log.trace("update method was executed with params: {} and result:{}", contact, result);
        return result;
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Contact> oldContacts = findAllByAccountId(oldAccountId);
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        for (Contact toDelete : oldContacts) {
            contactRepository.delete(toDelete);
            Contact toSave = new Contact();
            toSave.setAccountId(newAccountId);
            toSave.setMethod(toDelete.getMethod());
            toSave.setContactId(toDelete.getContactId());
            toSave.setNotificationName(toDelete.getNotificationName());
            contactRepository.save(toSave);
        }
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

    public List<Contact> findAllLikeNotificationName(String accountId, String notificationName) {
        List<Contact> byAccountId = findAllByAccountId(accountId);
        List<Contact> result = byAccountId
                .stream()
                .filter(contact -> contact.getNotificationName().startsWith(notificationName))
                .toList();
        log.trace("findAllLikeNotificationName method was executed with params: accountId-{}, notificationName-{} and result:{}", accountId, notificationName, result);
        return result;
    }

    public List<Contact> findAllByAccountId(String accountId) {
        List<Contact> result = contactRepository.findAllByAccountId(accountId);
        log.trace("findAllByUsername method was executed with params: accountId-{} and result:{}", accountId, result);
        return result;
    }

    private Contact getByKeyOrThrow(Contact contact) {
        return getByKeyOrThrow(contact.getAccountId(), contact.getMethod(), contact.getContactId());
    }

    private Contact getByKeyOrThrow(String accountId, Method method, String contactId) {
        Contact result;
        try {
            result = contactRepository.findById(new ContactCompositeKey(accountId, method, contactId)).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ContactDoesntExistException(e);
        }
        return result;
    }
}
