package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.TheaterNewRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterImageResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.model.TheaterImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITheaterMapper {
    @Mapping(target = "images", ignore = true)
    Theater toTheaterNewRequestToTheater(TheaterNewRequest theaterNewRequest);
    TheaterResponse toTheaterToTheaterResponse(Theater theater);
    TheaterImageResponse toTheaterImageToTheaterImageResponse(TheaterImage theaterImage);
    //    void updateTicketMovie(@MappingTarget Theater theater, TheaterUpdateRequest theaterUpdateRequest);
}
