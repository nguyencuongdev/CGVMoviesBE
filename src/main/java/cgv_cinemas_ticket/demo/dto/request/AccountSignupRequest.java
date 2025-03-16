package cgv_cinemas_ticket.demo.dto.request;

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
public class AccountSignupRequest {
    String name;
    String phoneNumber;
    String email;
    String password;
    String confirmPassword;
    Date dateOfBirth;
    boolean gender;
    String faviousCinemas;
}
