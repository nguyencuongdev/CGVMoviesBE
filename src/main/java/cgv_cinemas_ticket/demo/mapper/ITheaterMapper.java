package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.TheaterNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterImageResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.model.TheaterImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ITheaterMapper {
    @Mapping(target = "images", ignore = true)
    Theater toTheaterNewRequestToTheater(TheaterNewOrUpdateRequest theaterNewRequest);
    TheaterResponse toTheaterToTheaterResponse(Theater theater);
    TheaterImageResponse toTheaterImageToTheaterImageResponse(TheaterImage theaterImage);
    @Mapping(target = "images", ignore = true)
    void updateTheater(@MappingTarget Theater theater, TheaterNewOrUpdateRequest theaterUpdateRequest);
}
