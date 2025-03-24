package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.LevelNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.LevelResponse;
import cgv_cinemas_ticket.demo.model.Level;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ILevelMapper {
    Level toLevelNewOrUpdateRequestToLevel(LevelNewOrUpdateRequest levelNewOrUpdateRequest);
    LevelResponse toLevelToLevelResponse(Level level);
    void updateLevel(@MappingTarget Level level, LevelNewOrUpdateRequest levelUpdateRequest);
}
