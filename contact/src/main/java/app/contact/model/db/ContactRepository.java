package app.contact.model.db;

import app.contact.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, ContactCompositeKey> {
    List<Contact> findAllByAccountId(String accountId);
}
