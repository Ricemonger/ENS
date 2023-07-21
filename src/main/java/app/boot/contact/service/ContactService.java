package app.boot.contact.service;

import app.boot.contact.model.Contact;
import app.boot.contact.model.ContactCompositeKey;
import app.boot.contact.model.Method;
import app.boot.contact.service.repository.ContactRepository;
import app.boot.contact.controller.exceptions.ContactAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ContactRepository contactRepository;


    public Contact create(Contact contact) {
        try{
            contactRepository.findById(new ContactCompositeKey(contact.getUsername(),contact.getMethod(),contact.getContactId())).orElseThrow();
        }catch (NoSuchElementException e) {
            log.trace("create method was executed with params: {}",contact);
            return contactRepository.save(contact);
        }
        throw new ContactAlreadyExistsException();
    }

    public Contact delete(Contact contact) {
        Contact removed = contactRepository.findById(new ContactCompositeKey(contact.getUsername(), contact.getMethod(), contact.getContactId())).orElseThrow();
        contactRepository.delete(contact);
        log.trace("delete method was executed with params: {}",contact);
        return removed;
    }

    public Contact update(Contact contact) {
            Contact dbContact = contactRepository.findById(new ContactCompositeKey(contact.getUsername(), contact.getMethod(), contact.getContactId())).orElseThrow();
            dbContact.setNotificationName(contact.getNotificationName());
            Contact result = contactRepository.save(dbContact);
            log.trace("update method was executed with params: {} and result:{}",contact,result);
            return result;
    }

    public List<Contact> findAllByUsername(String username) {
        List<Contact> result = contactRepository.findAllByUsername(username);
        log.trace("findAllByUsername method was executed with params: username-{} and result:{}",username,result);
        return result;
    }

    public List<Contact> findAllLikePrimaryKey(String username, String method, String contactId) {
        List<Contact> byUsername = findAllByUsername(username);
        List<Contact> result = byUsername
                .stream()
                .filter(contact -> (contact.getMethod().name().equals(method) && contact.getContactId().startsWith(contactId)))
                .toList();
        log.trace("findAllLikePrimaryKey method was executed with params: {} and result:{}",new Contact(username,method,contactId),result);
        return result;
    }
    public Contact findOneByPrimaryKey(String username, String method, String contactId){
        Contact result = contactRepository.findById(new ContactCompositeKey(username, Method.valueOf(method.toUpperCase(Locale.ROOT)),contactId)).orElseThrow();
        log.trace("findOneByPrimaryKey method was executed with params: {} and result:{}",new Contact(username,method,contactId),result);
        return result;
    }

    public List<Contact> findAllLikeNotificationName(String username, String notificationName) {
        List<Contact> byUsername = findAllByUsername(username);
        List<Contact> result = byUsername
                .stream()
                .filter(contact -> contact.getNotificationName().startsWith(notificationName))
                .toList();
        log.trace("findAllLikeNotificationName method was executed with params: username-{}, notificationName-{} and result:{}",username,notificationName,result);
        return result;
    }
}
