package app.utils.services.contact.feign;

import app.utils.services.contact.exceptions.*;
import app.utils.services.security.exceptions.InvalidSecurityTokenException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ContactFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> {
                return new InvalidContactMethodException();
            }
            case 401 -> {
                return new InvalidSecurityTokenException();
            }
            case 403 -> {
                return new ContactAlreadyExistsException();
            }
            case 404 -> {
                return new ContactDoesntExistException();
            }
            case 500 -> {
                return new ContactInternalServerErrorException();
            }
            default -> {
                return new ContactUnspecifiedException();
            }
        }
    }
}
