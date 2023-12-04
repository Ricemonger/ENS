package app.utils.services.notification.feign;

import app.utils.services.notification.exceptions.NotificationAlreadyExistsException;
import app.utils.services.notification.exceptions.NotificationDoesntExistException;
import app.utils.services.notification.exceptions.NotificationInternalServerError;
import app.utils.services.notification.exceptions.NotificationUnspecifiedException;
import app.utils.services.security.exceptions.InvalidSecurityTokenException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class NotificationFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 401 -> {
                return new InvalidSecurityTokenException();
            }
            case 403 -> {
                return new NotificationAlreadyExistsException();
            }
            case 404 -> {
                return new NotificationDoesntExistException();
            }
            case 500 -> {
                return new NotificationInternalServerError();
            }
            default -> {
                return new NotificationUnspecifiedException();
            }
        }
    }
}
