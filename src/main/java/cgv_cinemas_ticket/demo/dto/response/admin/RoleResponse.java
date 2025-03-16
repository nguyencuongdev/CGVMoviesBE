package cgv_cinemas_ticket.demo.dto.response.admin;

import java.util.Set;

public class RoleResponse {
    Long id;
    String name;
    String note;
    Set<PermissionResponse> permissions;
}
