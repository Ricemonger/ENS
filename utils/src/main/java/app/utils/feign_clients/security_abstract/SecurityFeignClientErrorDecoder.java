package app.utils.feign_clients.security_abstract;

import app.utils.feign_clients.security_abstract.exceptions.InvalidSecurityTokenException;
import app.utils.feign_clients.security_abstract.exceptions.SecurityInternalServerError;
import app.utils.feign_clients.security_abstract.exceptions.SecurityUnknownException;
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
                return new SecurityUnknownException();
            }
        }
    }
}
