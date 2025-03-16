package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientUserResponse;
import cgv_cinemas_ticket.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    @Mapping(target = "faviousCinemas", ignore = true)
    User toAccountSigntoUser(AccountSignupRequest accountSignupRequest);
    ClientUserResponse toUserToClientUserResponse(User user);
}
