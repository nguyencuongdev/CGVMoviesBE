package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.CinemasNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.model.Cinemas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICinemasMapper {
    @Mapping(target = "theater", ignore = true)
    @Mapping(target = "cinemasType", ignore = true)
    Cinemas toCinemasNewOrUpdateRequestToCinemas(CinemasNewOrUpdateRequest cinemasNewOrUpdateRequest);
    CinemasResponse toCinemasToCinemasResponse(Cinemas cinemas);
    @Mapping(target = "theater", ignore = true)
    @Mapping(target = "cinemasType", ignore = true)
    void updateCinemas(@MappingTarget Cinemas cinemas, CinemasNewOrUpdateRequest cinemasUpdateRequest);
}
