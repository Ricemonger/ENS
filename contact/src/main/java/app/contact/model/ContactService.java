package app.contact.model;

import app.contact.exceptions.ContactAlreadyExistsException;
import app.contact.exceptions.ContactDoesntExistException;
import app.contact.model.db.ContactRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepositoryService contactRepositoryService;

    public Contact create(Contact contact) {
        try {
            getByKeyOrThrow(contact);
        } catch (ContactDoesntExistException e) {
            log.trace("create method was executed with params: {}", contact);
            return contactRepositoryService.save(contact);
        }
        throw new ContactAlreadyExistsException();
    }

    public Contact delete(Contact contact) {
        Contact removed = getByKeyOrThrow(contact);
        contactRepositoryService.delete(contact);
        log.trace("delete method was executed with params: {}", contact);
        return removed;
    }

    public void clear(String accountId) {
        List<Contact> toDelete = contactRepositoryService.findAllByAccountId(accountId);
        log.trace("clear method was called with accountId: {}", accountId);
        contactRepositoryService.deleteAll(toDelete);
    }

    public Contact update(Contact contact) {
        Contact dbContact = getByKeyOrThrow(contact);
        dbContact.setNotificationName(contact.getNotificationName());
        Contact result = contactRepositoryService.save(dbContact);
        log.trace("update method was executed with params: {} and result:{}", contact, result);
        return result;
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Contact> oldContacts = findAllByAccountId(oldAccountId);
        log.info("changeAccountId method is called with accountIDs old-{}, new-{}", oldAccountId, newAccountId);
        for (Contact toDelete : oldContacts) {
            contactRepositoryService.delete(toDelete);
            Contact toSave = new Contact();
            toSave.setAccountId(newAccountId);
            toSave.setMethod(toDelete.getMethod());
            toSave.setContactId(toDelete.getContactId());
            toSave.setNotificationName(toDelete.getNotificationName());
            contactRepositoryService.save(toSave);
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
        List<Contact> result = contactRepositoryService.findAllByAccountId(accountId);
        log.trace("findAllByUsername method was executed with params: accountId-{} and result:{}", accountId, result);
        return result;
    }

    private Contact getByKeyOrThrow(Contact contact) {
        return contactRepositoryService.findByIdOrThrow(contact.getAccountId(), contact.getMethod(), contact.getContactId());
    }
}
