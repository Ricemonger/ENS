package app.boot.contact.controller;

public class ContactAlreadyExistsException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Contact with same creator's username, delivery method and contact's id already exists";
    }
}
