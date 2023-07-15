package app.boot.contact.service.repository;

import app.boot.contact.model.Contact;
import app.boot.contact.model.ContactCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, ContactCompositeKey> {
    List<Contact> findAllByUsername(String username);
}
