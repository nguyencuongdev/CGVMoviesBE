package cgv_cinemas_ticket.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // Trong enum có thể có hàm contractor và các property. Các hằng số trong enum và contractor được ngăn cách boi dau chấm phảy
    // đây là các hằng số của enum với mỗi enum là một instance của enum (đây là syntax gọi contractor trong enum)
    // (9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR) -> khởi tạo các giá trị property của một hằng số enum
    INTERNAL_SERVER_ERROR(1000, "server-internal-error", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED(1002, "method-not-support", HttpStatus.METHOD_NOT_ALLOWED),
    ENDPOINT_NOT_FOUND(1003, "Endpoint of request not found!", HttpStatus.NOT_FOUND),
    MULTIPART_FILE_NULL(1004, "Request required a file!", HttpStatus.BAD_REQUEST),
    FILE_DELETED_FAILED(1005, "File deleted failed!", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1006, "File not found!", HttpStatus.NOT_FOUND),
    FILE_INVALID(1007, "File must is types: image/png, image/jpeg, image/jpeg, image/webp and file size <= 5MB",
            HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED(1008, "Authentication failed!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "You do not have permission!", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_MATCH(1010, "Password is incorrect!", HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_EXITED(1016, "Account not available!", HttpStatus.UNAUTHORIZED),
    PERMISSION_ALREADY_EXISTS(1011, "Permission already exists", HttpStatus.CONFLICT),
    USER_DENIED_AUTHORIZED(1012, "User denied authorization", HttpStatus.UNAUTHORIZED),
    USER_CODE_EXPIRED(1013, "Code of user expired", HttpStatus.UNAUTHORIZED),
    USER_TOKEN_EXPIRED(1014, "Token of user expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(1015, "Refresh token failed!", HttpStatus.LOCKED),
    LEVEL_NOT_EXISTED(1016, "level-not-existed", HttpStatus.NOT_FOUND),
    POPCOM_NOT_EXISTED(1017, "popcom-not-existed", HttpStatus.NOT_FOUND),
    TICKET_MOVIE_NOT_EXISTED(1018, "ticket_movie-not-existed", HttpStatus.NOT_FOUND),
    THEATER_NOT_EXISTED(1019, "theater-not-existed", HttpStatus.NOT_FOUND),
    CINEMASTYPE_NOT_EXISTED(1120, "cinemas-type-not-existed", HttpStatus.NOT_FOUND),
    CINEMAS_NOT_EXISTED(1121, "cinemas-not-existed", HttpStatus.NOT_FOUND);

    private ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
