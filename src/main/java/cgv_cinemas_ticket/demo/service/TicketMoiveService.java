package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllPopcomFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllTicketMovieFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TicketMovieResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ITicketMovieMapper;
import cgv_cinemas_ticket.demo.model.Popcom;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import cgv_cinemas_ticket.demo.repository.ITicketMovieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public DataListResponseWithPagination<List<TicketMovieResponse>> handleGetAllTicketMovie(PaginationRequestParams paginationParams, GetAllTicketMovieFilterParams filterParams) {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<TicketMovie> ticketMovieListMatchedFilterLimited = new ArrayList<>();
        List<TicketMovie> ticketMovieListMatchedFilter = new ArrayList<>();
        List<TicketMovie> ticketMovieListInDB = new ArrayList<>();

        if (Objects.isNull(filterParams.getSearchValue())) {
            ticketMovieListInDB = ticketMovieRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            ticketMovieListInDB = ticketMovieRepository.findAllByNameContainingIgnoreCase(searchValue);
        }
        List<String> filterByFields = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFields.add("status");


        for (TicketMovie ticketMovie : ticketMovieListInDB) {
            boolean checkMatch = isCheckTicketMovieMatchFilter(filterParams, ticketMovie, filterByFields);
            if (checkMatch) {
                ticketMovieListMatchedFilter.add(ticketMovie);
            }
        }
        int totalElements = ticketMovieListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());

        // Ticket movie list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                ticketMovieListMatchedFilterLimited.add(ticketMovieListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            ticketMovieListMatchedFilterLimited = ticketMovieListMatchedFilter;
        }

        List<TicketMovieResponse> ticketMovieRespostList = ticketMovieListMatchedFilterLimited.stream().map(ticketMovieMapper::toTicketMovieToTicketMovieResponse).toList();
        return DataListResponseWithPagination.<List<TicketMovieResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(ticketMovieRespostList)
                .build();
    }

    public TicketMovieResponse handleUpdateTicketMovie(String id, TicketMovieUpdateRequest ticketMovieUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));

        ticketMovieMapper.updateTicketMovie(ticketMovie,ticketMovieUpdateRequest);
        ticketMovieRepository.save(ticketMovie);
        return ticketMovieMapper.toTicketMovieToTicketMovieResponse(ticketMovie);
    }

    private static boolean isCheckTicketMovieMatchFilter(GetAllTicketMovieFilterParams filterParams, TicketMovie ticketMovie, List<String> filterByFileds) {
        boolean checkMatch = true;
        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == ticketMovie.isStatus();
        }
        return checkMatch;
    }

//    public boolean handleDeleteTicketMovie(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.TICKET_MOVIE_NOT_EXISTED;
//        TicketMovie ticketMovie = ticketMovieRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        ticketMovieRepository.deleteById(ticketMovie.getId());
//        return true;
//    }
}
