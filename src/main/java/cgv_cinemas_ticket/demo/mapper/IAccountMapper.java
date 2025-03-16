package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.AccountResponse;
import cgv_cinemas_ticket.demo.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAccountMapper {
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "user", ignore = true)
    Account toAccountSignupToAccount(AccountSignupRequest accountSignupRequest);

    AccountResponse toAccountToAccountResponse(Account account);
}
