package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientAccountResponse;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientRoleResponse;
import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    ClientRoleResponse toRoleToClientRoleResponse(Role role);
}
