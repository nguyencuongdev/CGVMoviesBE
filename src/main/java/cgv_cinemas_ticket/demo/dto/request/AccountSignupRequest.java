package cgv_cinemas_ticket.demo.dto.request;

import cgv_cinemas_ticket.demo.validation.EmailUniqueConstranit;
import cgv_cinemas_ticket.demo.validation.PasswordMatchConstraint;
import cgv_cinemas_ticket.demo.validation.PhoneNumberUniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatchConstraint(message = "CONFIRM_PASSWORD_NOT_MATCH")
public class AccountSignupRequest {
    @NotBlank(message = "REQUIRED")
    @Size(min = 6, max = 255, message = "USER_NAME_INVALID")
    String name;
    @NotBlank(message = "REQUIRED")
    @Size(min = 10, max = 10, message = "PHONE_NUMBER_INVALID")
    @PhoneNumberUniqueConstraint(message = "PHONE_NUMBER_EXITED_WITH_ACCOUNT")
    String phoneNumber;
    @NotBlank(message = "REQUIRED")
    @Size(max = 255, message = "EMAIL_INVALID")
    @EmailUniqueConstranit(message = "EMAIL_EXITED_WITH_ACCOUNT")
    String email;
    @NotBlank(message = "REQUIRED")
    @Size(min = 8, max = 50, message = "PASSWORD_INVALID")
    String password;
    @NotBlank(message = "REQUIRED")
    @Size(min = 8, max = 50, message = "PASSWORD_INVALID")
    String confirmPassword;
    Date dateOfBirth;
    boolean gender;
//    String faviousCinemas;
}
