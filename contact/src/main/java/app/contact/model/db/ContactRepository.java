package app.contact.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<ContactEntity, ContactCompositeKey> {
    List<ContactEntity> findAllByAccountId(String accountId);
}
