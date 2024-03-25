package app.utils.services.sender.feign;

import app.utils.services.contact.exceptions.InvalidContactMethodException;
import app.utils.services.sender.exceptions.InvalidContactIdException;
import app.utils.services.sender.exceptions.SenderApiException;
import app.utils.services.sender.exceptions.SenderInternalErrorException;
import app.utils.services.sender.exceptions.SenderUnspecifiedException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class SenderFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> {
                return new InvalidContactMethodException();
            }
            case 401 -> {
                return new SenderApiException();
            }
            case 403 -> {
                return new InvalidContactIdException();
            }
            case 500 -> {
                return new SenderInternalErrorException();
            }
            default -> {
                return new SenderUnspecifiedException();
            }
        }
    }
}
