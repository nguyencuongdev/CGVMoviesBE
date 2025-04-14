package cgv_cinemas_ticket.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationException extends Exception {
    int statusCode;
    Map<String, String> errors;

    public ValidationException(String message, int statusCode, Map<String, String> errors) {
        super(message);
        this.statusCode = statusCode;
        this.errors = errors;
    }
}
