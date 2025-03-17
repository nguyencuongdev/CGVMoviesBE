package cgv_cinemas_ticket.demo.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum MessageResponse {
    VERIFY_EMAIL_ACCOUNT_SUCCESS("verify-email-account-success"),
    SENT_EMAIL_VERIFY_ACCOUNT_SUCCESS("verify-email-account-success"),
    REFRESH_TOKEN_SUCCESS("refresh-token-success"),
    AUTHENTICATION_SUCCESS("authentication-success"),
    LOGOUT_SUCCESS("logout-success"),
    SIGNUP_ACCOUNT_SUCCESS("signup-account-success"),
    SIGNUP_ACCOUNT_FAILED("signup-account-failed"),
    ;
    private final String message;
}
