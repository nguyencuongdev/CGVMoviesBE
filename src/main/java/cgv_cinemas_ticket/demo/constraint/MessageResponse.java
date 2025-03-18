package cgv_cinemas_ticket.demo.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum MessageResponse {
    //    Constraint common
    VERIFY_EMAIL_ACCOUNT_SUCCESS("verify-email-account-success"),
    SENT_EMAIL_VERIFY_ACCOUNT_SUCCESS("verify-email-account-success"),
    REFRESH_TOKEN_SUCCESS("refresh-token-success"),
    AUTHENTICATION_SUCCESS("authentication-success"),
    LOGOUT_SUCCESS("logout-success"),
    SIGNUP_ACCOUNT_SUCCESS("signup-account-success"),
    SIGNUP_ACCOUNT_FAILED("signup-account-failed"),
    //    Constraints for page content management
    LEVEL_ADD_NEW_SUCCESS("level-add-new-success"),
    LEVEL_GET_ALL_SUCCESS("level-get-all-success"),
    LEVEL_UPDATE_SUCCESS("level-update-success"),
    LEVEL_DELETE_SUCCESS("level-delete-success"),
    POPCOM_ADD_NEW_SUCCESS("popcom-add-new-success"),
    POPCOM_GET_ALL_SUCCESS("popcom-get-all-success"),
    POPCOM_UPDATE_SUCCESS("popcom-update-success"),
    POPCOM_DELETE_SUCCESS("popcom-delete-success"),
    ;
    private final String message;
}
