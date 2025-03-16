package cgv_cinemas_ticket.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountLoginRequest {
    @NotBlank(message = "ACCOUNT_REQUIRED")
    String account;
    @NotBlank(message = "ACCOUNT_REQUIRED")
    @Size(min = 8, max = 50, message = "PASSWORD_INVALID")
    String password;
}
