package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.TicketMovieResponse;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ITicketMovieMapper {
    TicketMovie toTicketMovieNewRequestToTicketMovie(TicketMovieNewRequest ticketMovieNewRequest);
    TicketMovieResponse toTicketMovieToTicketMovieResponse(TicketMovie ticketMovie);
    void updateTicketMovie(@MappingTarget TicketMovie ticketMovie, TicketMovieUpdateRequest ticketMovieUpdateRequest);
}
