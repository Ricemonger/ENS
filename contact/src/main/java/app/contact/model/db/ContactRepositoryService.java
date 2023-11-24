package app.contact.model.db;

import app.contact.model.Contact;
import app.utils.feign_clients.contact.Method;
import app.utils.feign_clients.contact.exceptions.ContactDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactRepositoryService {

    private final ContactRepository contactRepository;

    public Contact save(Contact contact) {
        Contact result = toContact(contactRepository.save(toEntity(contact)));
        log.trace("save was executed for contact-{}", contact);
        return result;
    }

    public Contact findByIdOrThrow(String accountId, Method method, String contactId) {
        ContactCompositeKey key = new ContactCompositeKey(accountId, method, contactId);
        try {
            Contact contact = toContact(contactRepository.findById(key).orElseThrow());
            log.trace("findByIdOrThrow was executed with key-{} and result-{}", key, contact);
            return contact;
        } catch (NoSuchElementException e) {
            log.trace("findByIdOrThrow couldn't find contact with key-{}", key);
            throw new ContactDoesntExistException();
        }
    }

    public List<Contact> findAllByAccountId(String accountId) {
        List<Contact> contacts = contactRepository.findAllByAccountId(accountId).stream().map(this::toContact).toList();
        log.trace("findAllByAccountId was executed with accountId-{} and result-{}", accountId, contacts);
        return contacts;
    }

    public List<Contact> findAll() {
        log.trace("findAll was executed");
        return contactRepository.findAll().stream().map(this::toContact).toList();
    }

    public void delete(Contact contact) {
        contactRepository.delete(toEntity(contact));
        log.trace("delete was executed for contact-{}", contact);
    }

    public void deleteAll(List<Contact> toDelete) {
        List<ContactEntity> entitiesToDelete = toDelete.stream().map(this::toEntity).toList();
        contactRepository.deleteAll(entitiesToDelete);
        log.trace("deleteAll was executed for list-{}", toDelete);
    }

    public void deleteAll() {
        contactRepository.deleteAll();
        log.trace("deleteAll was executed");
    }

    private ContactEntity toEntity(Contact contact) {
        return new ContactEntity(contact.getAccountId(), contact.getMethod(), contact.getContactId(),
                contact.getNotificationName());
    }

    private Contact toContact(ContactEntity entity) {
        return new Contact(entity);
    }
}
