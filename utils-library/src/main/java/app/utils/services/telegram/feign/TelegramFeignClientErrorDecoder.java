package app.utils.services.telegram.feign;

import app.utils.services.security.exceptions.InvalidSecurityTokenException;
import app.utils.services.security.exceptions.SecurityInternalServerError;
import app.utils.services.security.exceptions.SecurityUnspecifiedException;
import app.utils.services.telegram.exceptions.TelegramUserAlreadyExistsException;
import app.utils.services.telegram.exceptions.TelegramUserDoesntExistException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class TelegramFeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 401 -> {
                return new InvalidSecurityTokenException();
            }
            case 403 -> {
                return new TelegramUserAlreadyExistsException();
            }
            case 404 -> {
                return new TelegramUserDoesntExistException();
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
