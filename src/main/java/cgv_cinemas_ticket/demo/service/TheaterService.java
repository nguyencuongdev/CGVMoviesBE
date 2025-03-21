package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.TheaterNewRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterImageResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.mapper.ITheaterMapper;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.model.TheaterImage;
import cgv_cinemas_ticket.demo.repository.IFileTempRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterImageRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TheaterService {
    ITheaterRepository theaterRepository;
    ITheaterImageRepository theaterImageRepository;
    IFileTempRepository fileTempRepository;
    ITheaterMapper theaterMapper;

    public TheaterResponse handleAddNewTheater(TheaterNewRequest theaterNewRequest) {
        Theater theater = theaterMapper.toTheaterNewRequestToTheater(theaterNewRequest);
        Set<TheaterImage> theaterImageList = new HashSet<>();
        theaterNewRequest.getImages().forEach(fileImage -> {
            TheaterImage theaterImage = TheaterImage.builder()
                    .srcImg(fileImage.getSrc())
                    .fileName(fileImage.getFileName())
                    .theater(theater)
                    .build();
            theaterImageList.add(theaterImage);
        });
        theater.setStatus(true);
        theater.setCreateAt(new Date());
        theater.setUpdateAt(new Date());
        theaterRepository.save(theater);
        Set<TheaterImageResponse> theaterImageResponseList = theaterImageRepository.saveAll(theaterImageList)
                .stream()
                .map(theaterMapper::toTheaterImageToTheaterImageResponse)
                .collect(Collectors.toSet());
        TheaterResponse theaterResponse = theaterMapper.toTheaterToTheaterResponse(theater);
        theaterResponse.setImages(theaterImageResponseList);
        return theaterMapper.toTheaterToTheaterResponse(theater);
    }

    public List<TheaterResponse> handleGetAllTheater() {
        List<Theater> theaterList = theaterRepository.findAll();
        return theaterList.stream().map(theaterMapper::toTheaterToTheaterResponse).toList();
    }
//
//    public TicketMovieResponse handleUpdateTicketMovie(String id, TicketMovieUpdateRequest ticketMovieUpdateRequest) throws AppException {
//        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
//        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//
//        ticketMovieMapper.updateTicketMovie(ticketMovie,ticketMovieUpdateRequest);
//        ticketMovieRepository.save(ticketMovie);
//        return ticketMovieMapper.toTicketMovieToTicketMovieResponse(ticketMovie);
//    }
//    public boolean handleDeleteTicketMovie(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
//        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        ticketMovieRepository.deleteById(ticketMovie.getId());
//        return true;
//    }
}
