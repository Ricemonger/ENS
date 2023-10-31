package app.contact.service.db;

import app.contact.service.Contact;
import app.contact.service.ContactCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, ContactCompositeKey> {
    List<Contact> findAllByAccountId(String accountId);
}
