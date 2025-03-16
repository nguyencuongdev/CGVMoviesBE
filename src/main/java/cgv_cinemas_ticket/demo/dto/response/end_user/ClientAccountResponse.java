package cgv_cinemas_ticket.demo.dto.response.end_user;

import cgv_cinemas_ticket.demo.model.Level;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientAccountResponse {
    Long id;
    String email;
    String phoneNumber;
    boolean status;
    int currentPoint;
    Level level;
    ClientUserResponse user;
    Set<ClientRoleResponse> roles;
}
