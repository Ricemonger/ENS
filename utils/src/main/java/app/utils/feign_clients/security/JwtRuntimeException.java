package app.utils.feign_clients.security;

public class JwtRuntimeException extends RuntimeException {

    public JwtRuntimeException() {
        super();
    }

    public JwtRuntimeException(Throwable cause) {
        super(cause);
    }

    public JwtRuntimeException(String message) {
        super(message);
    }
}
