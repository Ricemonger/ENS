package app.contact.model.db;

import app.contact.model.Contact;
import app.utils.services.contact.Method;
import app.utils.services.contact.exceptions.ContactDoesntExistException;
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
        return toContact(contactRepository.save(toEntity(contact)));
    }

    public boolean doesContactExists(Contact contact) {
        ContactCompositeKey key = new ContactCompositeKey(contact.getAccountId(), contact.getMethod(),
                contact.getContactId());
        return contactRepository.existsById(key);
    }

    public Contact findByIdOrThrow(String accountId, Method method, String contactId) {
        ContactCompositeKey key = new ContactCompositeKey(accountId, method, contactId);
        try {
            return toContact(contactRepository.findById(key).orElseThrow());
        } catch (NoSuchElementException e) {
            log.info("findByIdOrThrow executed for key-{}, contact doesnt exists", key);
            throw new ContactDoesntExistException();
        }
    }

    public List<Contact> findAllByAccountId(String accountId) {
        return contactRepository.findAllByAccountId(accountId).stream().map(this::toContact).toList();
    }

    public List<Contact> findAll() {
        return contactRepository.findAll().stream().map(this::toContact).toList();
    }

    public void delete(Contact contact) {
        contactRepository.delete(toEntity(contact));
    }

    public void deleteAll() {
        contactRepository.deleteAll();
    }

    public void deleteAll(List<Contact> toDelete) {
        List<ContactEntity> entitiesToDelete = toDelete.stream().map(this::toEntity).toList();
        contactRepository.deleteAll(entitiesToDelete);
    }

    private ContactEntity toEntity(Contact contact) {
        return new ContactEntity(contact.getAccountId(), contact.getMethod(), contact.getContactId(),
                contact.getNotificationName());
    }

    private Contact toContact(ContactEntity entity) {
        return new Contact(entity);
    }
}
