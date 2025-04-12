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
    TICKET_MOVIE_ADD_NEW_SUCCESS("ticket-movie-add-new-success"),
    TICKET_MOVIE_GET_ALL_SUCCESS("ticket-movie-get-all-success"),
    TICKET_MOVIE_UPDATE_SUCCESS("ticket-movie-update-success"),
    TICKET_MOVIE_DELETE_SUCCESS("ticket-movie-delete-success"),
    THEATER_ADD_NEW_SUCCESS("theater-add-new-success"),
    THEATER_GET_ALL_SUCCESS("theater-get-all-success"),
    THEATER_UPDATE_SUCCESS("theater-update-success"),
    THEATER_DELETE_SUCCESS("theater-delete-success"),
    CINEMASTYPE_ADD_NEW_SUCCESS("cinemas-type-add-new-success"),
    CINEMASTYPE_GET_ALL_SUCCESS("cinemas-type-get-all-success"),
    CINEMASTYPE_UPDATE_SUCCESS("cinemas-type-update-success"),
    CINEMASTYPE_DELETE_SUCCESS("cinemas-type-delete-success"),
    CINEMAS_ADD_NEW_SUCCESS("cinemas-add-new-success"),
    CINEMAS_GET_ALL_SUCCESS("cinemas-get-all-success"),
    CINEMAS_UPDATE_SUCCESS("cinemas-update-success"),
    CINEMAS_DELETE_SUCCESS("cinemas-delete-success"),
    MOVIE_CATEGORY_ADD_NEW_SUCCESS("movie-category-add-new-success"),
    MOVIE_CATEGORY_GET_ALL_SUCCESS("movie-category-get-all-success"),
    MOVIE_CATEGORY_UPDATE_SUCCESS("movie-category-update-success"),
    ;
    private final String message;
}
