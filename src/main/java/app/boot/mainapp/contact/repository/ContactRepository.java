package app.boot.mainapp.contact.repository;

import app.boot.mainapp.contact.model.Contact;
import app.boot.mainapp.contact.model.ContactCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, ContactCompositeKey> {
    List<Contact> findAllByUsername(String username);
}
