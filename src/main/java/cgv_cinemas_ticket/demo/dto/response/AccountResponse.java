package cgv_cinemas_ticket.demo.dto.response;

import cgv_cinemas_ticket.demo.dto.response.end_user.ClientRoleResponse;
import cgv_cinemas_ticket.demo.model.Level;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Long id;
    String email;
    String phoneNumber;
    boolean status;
    int currentPoint;
    Date createAt;
    Date updateAt;
    Level level;
    UserResponse user;
    Set<ClientRoleResponse> roles;
}
