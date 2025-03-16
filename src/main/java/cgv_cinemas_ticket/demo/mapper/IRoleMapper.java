package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.response.end_user.ClientRoleResponse;
import cgv_cinemas_ticket.demo.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    ClientRoleResponse toRoleToClientRoleResponse(Role role);
}
