package app.utils.services.security.telegram.feign;

import app.utils.services.security.exceptions.*;
import feign.Response;
import feign.codec.ErrorDecoder;

public class SecurityTelegramFeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 401 -> {
                return new InvalidSecurityTokenException();
            }
            case 403 -> {
                return new UserAlreadyExistsException();
            }
            case 404 -> {
                return new UserDoesntExistException();
            }
            case 500 -> {
                return new SecurityInternalServerError();
            }
            default -> {
                return new SecurityUnspecifiedException();
            }
        }
    }
}
