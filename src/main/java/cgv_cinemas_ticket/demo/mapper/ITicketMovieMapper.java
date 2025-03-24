package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.TicketMovieResponse;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ITicketMovieMapper {
    TicketMovie toTicketMovieNewRequestToTicketMovie(TicketMovieNewRequest ticketMovieNewRequest);
    TicketMovieResponse toTicketMovieToTicketMovieResponse(TicketMovie ticketMovie);
    void updateTicketMovie(@MappingTarget TicketMovie ticketMovie, TicketMovieUpdateRequest ticketMovieUpdateRequest);
}
