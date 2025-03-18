package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.request.LevelNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.AccountResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.LevelResponse;
import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.model.Level;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ILevelMapper {
    Level toLevelNewOrUpdateRequestToLevel(LevelNewOrUpdateRequest levelNewOrUpdateRequest);
    LevelResponse toLevelToLevelResponse(Level level);
    void updateLevel(@MappingTarget Level level, LevelNewOrUpdateRequest levelUpdateRequest);
}
