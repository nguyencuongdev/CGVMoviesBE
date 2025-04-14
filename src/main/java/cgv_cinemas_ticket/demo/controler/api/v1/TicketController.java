package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TicketMovieResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.TicketMoiveService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {
    TicketMoiveService ticketMovieService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<TicketMovieResponse>> addNewTicketMovie(@RequestBody @Valid TicketMovieNewRequest ticketNewRequest) {
        MessageResponse messageResponse = MessageResponse.TICKET_MOVIE_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<TicketMovieResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                        .data(ticketMovieService.handleAddNewTicketMovie(ticketNewRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<TicketMovieResponse>>> getTicketMovies() {
        MessageResponse messageResponse = MessageResponse.TICKET_MOVIE_GET_ALL_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<List<TicketMovieResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(ticketMovieService.handleGetAllTicketMovie())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<TicketMovieResponse>> updateTicketMovie(@PathVariable String id,@RequestBody @Valid TicketMovieUpdateRequest ticketUpdateRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.TICKET_MOVIE_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<TicketMovieResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(ticketMovieService.handleUpdateTicketMovie(id, ticketUpdateRequest))
                .build());
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<TicketMovieResponse>> deleteTicketMovie(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.TICKET_MOVIE_DELETE_SUCCESS;
//        ticketMovieService.handleDeleteTicketMovie(id);
//        return ResponseEntity.ok(ApiResponse.<TicketMovieResponse>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
