package utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionMessage {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    private ExceptionMessage() {
        timestamp = LocalDateTime.now();
    }

    public ExceptionMessage(HttpStatus status) {
        this();
        this.status = status;
    }

    public ExceptionMessage(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public ExceptionMessage(HttpStatus status, Throwable ex) {
        this(status);
        this.message = ex.getClass() + " occurred: " + ex.getLocalizedMessage();
    }

    public ExceptionMessage(HttpStatus status, String message, Throwable ex) {
        this(status);
        this.message = message + ": " + ex.getLocalizedMessage();
    }
}
