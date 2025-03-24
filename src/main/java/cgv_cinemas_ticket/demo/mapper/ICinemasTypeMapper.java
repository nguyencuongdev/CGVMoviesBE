package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.CinemasTypeNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasTypeResponse;
import cgv_cinemas_ticket.demo.model.CinemasType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICinemasTypeMapper {
    @Mapping(target = "avatar", ignore = true)
    CinemasType toCinemasTypeNewOrUpdateRequestToCinemasType(CinemasTypeNewOrUpdateRequest cinemasTypeNewOrUpdateRequest);
    CinemasTypeResponse toCinemasTypeToCinemasTypeResponse(CinemasType cinemasType);
    @Mapping(target = "avatar", ignore = true)
    void updateCinemasType(@MappingTarget CinemasType cinemasType, CinemasTypeNewOrUpdateRequest cinemasTypeUpdateRequest);
}
