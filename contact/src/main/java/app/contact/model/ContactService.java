package app.contact.model;

import app.contact.model.db.ContactRepositoryService;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.contact.exceptions.ContactAlreadyExistsException;
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
        if (!doesContactExists(contact)) {
            return contactRepositoryService.save(contact);
        } else {
            log.info("create executed for contact-{}, contact already exists", contact);
            throw new ContactAlreadyExistsException();
        }
    }

    public Contact update(Contact contact) {
        Contact dbContact = getByKeyOrThrow(contact);
        dbContact.setNotificationName(contact.getNotificationName());
        return contactRepositoryService.save(dbContact);
    }

    public Contact delete(Contact contact) {
        Contact removed = getByKeyOrThrow(contact);
        contactRepositoryService.delete(contact);
        return removed;
    }

    public void clear(String accountId) {
        List<Contact> toDelete = contactRepositoryService.findAllByAccountId(accountId);
        contactRepositoryService.deleteAll(toDelete);
    }

    public void changeAccountId(String oldAccountId, String newAccountId) {
        List<Contact> oldContacts = findAllByAccountId(oldAccountId);
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
        return result;
    }

    public List<Contact> findAllLikeNotificationName(String accountId, String notificationName) {
        List<Contact> byAccountId = findAllByAccountId(accountId);
        return byAccountId
                .stream()
                .filter(contact -> contact.getNotificationName().startsWith(notificationName))
                .toList();
    }

    public List<Contact> findAllByAccountId(String accountId) {
        return contactRepositoryService.findAllByAccountId(accountId);
    }

    private Contact getByKeyOrThrow(Contact contact) {
        log.debug("getByKeyOrThrow called for contact-{}", contact);
        Contact result = contactRepositoryService.findByIdOrThrow(contact.getAccountId(), contact.getMethod(),
                contact.getContactId());
        log.trace("getByKeyOrThrow executed for contact-{} with result-{}", contact, result);
        return result;
    }

    private boolean doesContactExists(Contact contact) {
        log.debug("doesUserExists called for contact-{}", contact);
        boolean result = contactRepositoryService.doesContactExists(contact);
        log.trace("doesUserExists executed for contact-{} with result-{}", contact, result);
        return result;
    }
}
