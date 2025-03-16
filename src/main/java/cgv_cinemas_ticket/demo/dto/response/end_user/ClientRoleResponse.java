package cgv_cinemas_ticket.demo.dto.response.end_user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRoleResponse {
    Long id;
    String name;
    Set<ClientPermissionResponse> permissions;
}
