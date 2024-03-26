package app.utils.services.contact.exceptions;

public class ContactInternalServerErrorException extends RuntimeException {
    public ContactInternalServerErrorException() {
        super();
    }

    public ContactInternalServerErrorException(Throwable cause) {
        super(cause);
    }

    public ContactInternalServerErrorException(String message) {
        super(message);
    }
}
