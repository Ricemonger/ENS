package app.utils.feign_clients.sender;

import feign.Response;
import feign.codec.ErrorDecoder;

public class SenderFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            // Handle 404 Not Found
        }

        // Handle other HTTP status codes or generic exceptions
        return new Exception("Generic error occurred");
    }
}
