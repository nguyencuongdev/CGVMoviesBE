package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.TicketMovieResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ITicketMovieMapper;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import cgv_cinemas_ticket.demo.repository.ITicketMovieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TicketMoiveService {
    ITicketMovieRepository ticketMovieRepository;
    ITicketMovieMapper ticketMovieMapper;

    public TicketMovieResponse handleAddNewTicketMovie(TicketMovieNewRequest ticketMovieNewRequest) {
        TicketMovie ticketMovie = ticketMovieMapper.toTicketMovieNewRequestToTicketMovie(ticketMovieNewRequest);
        ticketMovieRepository.save(ticketMovie);
        return ticketMovieMapper.toTicketMovieToTicketMovieResponse(ticketMovie);
    }

    public List<TicketMovieResponse> handleGetAllTicketMovie() {
        List<TicketMovie> tickets = ticketMovieRepository.findAll();
        return tickets.stream().map(ticketMovieMapper::toTicketMovieToTicketMovieResponse).toList();
    }

    public TicketMovieResponse handleUpdateTicketMovie(String id, TicketMovieUpdateRequest ticketMovieUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));

        ticketMovieMapper.updateTicketMovie(ticketMovie,ticketMovieUpdateRequest);
        ticketMovieRepository.save(ticketMovie);
        return ticketMovieMapper.toTicketMovieToTicketMovieResponse(ticketMovie);
    }
    public boolean handleDeleteTicketMovie(String id) throws AppException {
        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
        ticketMovieRepository.deleteById(ticketMovie.getId());
        return true;
    }
}
