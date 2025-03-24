package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.model.Popcom;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IPopcomMapper {
    Popcom toPopcomNewOrUpdateRequestToPopcom(PopcomNewOrUpdateRequest popcomNewOrUpdateRequest);
    PopcomResponse toPopcomToPopcomResponse(Popcom popcom);
    void updatePopcom(@MappingTarget Popcom popcom, PopcomNewOrUpdateRequest popcomUpdateRequest);
}
