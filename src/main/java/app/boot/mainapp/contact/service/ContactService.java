package app.boot.mainapp.contact.service;

import app.boot.mainapp.contact.controller.ContactAlreadyExistsException;
import app.boot.mainapp.contact.model.Contact;
import app.boot.mainapp.contact.model.ContactCompositeKey;
import app.boot.mainapp.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;


    public Contact create(Contact contact) {
        try{
            contactRepository.findById(new ContactCompositeKey(contact.getUsername(),contact.getMethod(),contact.getContactId())).orElseThrow();
        }catch (NoSuchElementException e) {
            return contactRepository.save(contact);
        }
        throw new ContactAlreadyExistsException();
    }

    public Contact delete(Contact contact) {
        Contact removed = contactRepository.findById(new ContactCompositeKey(contact.getUsername(), contact.getMethod(), contact.getContactId())).orElseThrow();
        contactRepository.delete(contact);
        return removed;
    }

    public Contact update(Contact contact) {
            Contact dbContact = contactRepository.findById(new ContactCompositeKey(contact.getUsername(), contact.getMethod(), contact.getContactId())).orElseThrow();
            dbContact.setNotificationName(contact.getNotificationName());
            return contactRepository.save(dbContact);
    }

    public List<Contact> findAllByUsername(String username) {
        return contactRepository.findAllByUsername(username);
    }

    public List<Contact> findAllLikePrimaryKey(String username, String method, String contactId) {
        List<Contact> byUsername = findAllByUsername(username);
        return byUsername
                .stream()
                .filter(contact -> (contact.getMethod().name().equals(method) && contact.getContactId().startsWith(contactId)))
                .toList();
    }

    public List<Contact> findAllLikeNotificationName(String username, String notificationName) {
        List<Contact> byUsername = findAllByUsername(username);
        return byUsername
                .stream()
                .filter(contact -> contact.getNotificationName().startsWith(notificationName))
                .toList();
    }
}
