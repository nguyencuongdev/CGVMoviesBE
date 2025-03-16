package cgv_cinemas_ticket.demo.exception;

import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .status(false)
                        .statusCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .status(false)
                        .statusCode(ex.getStatusCode())
                        .message(ex.getMessage())
                        .build()
        );
    }
}
