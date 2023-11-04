package app.telegram.service.contact;

import app.utils.contact.Contact;
import app.utils.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceWrapper {

    private final ContactService contactService;

    public List<Contact> findAll(Long chatId) {


    }

    public void addMany(List<Contact> contacts) {

    }

    public void addOne(Contact contact) {

    }

    public void removeMany(List<Contact> contacts) {

    }

    public void removeOne(Contact contact) {

    }

    public void clear(Long chatId) {

    }
}
