package app.contact.service.repository;

import app.contact.model.Contact;
import app.contact.model.ContactCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, ContactCompositeKey> {
    List<Contact> findAllByUsername(String username);
}
