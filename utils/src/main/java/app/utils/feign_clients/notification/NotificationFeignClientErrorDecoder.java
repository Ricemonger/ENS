package app.utils.feign_clients.notification;

import app.utils.feign_clients.notification.exceptions.NotificationAlreadyExistsException;
import app.utils.feign_clients.notification.exceptions.NotificationDoesntExistException;
import app.utils.feign_clients.notification.exceptions.NotificationInternalServerError;
import app.utils.feign_clients.notification.exceptions.NotificationUnknownException;
import app.utils.feign_clients.security_abstract.exceptions.InvalidSecurityTokenException;
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
                return new NotificationUnknownException();
            }
        }
    }
}
