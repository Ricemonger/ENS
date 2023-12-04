package app.utils.services.security.abstact.feign;

import app.utils.services.security.exceptions.InvalidSecurityTokenException;
import app.utils.services.security.exceptions.SecurityInternalServerError;
import app.utils.services.security.exceptions.SecurityUnspecifiedException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class SecurityFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 401 -> {
                return new InvalidSecurityTokenException();
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
