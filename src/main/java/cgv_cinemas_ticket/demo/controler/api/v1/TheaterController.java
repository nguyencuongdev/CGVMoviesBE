package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.TheaterNewRequest;
import cgv_cinemas_ticket.demo.dto.request.TicketMovieNewRequest;
import cgv_cinemas_ticket.demo.dto.request.TicketMovieUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.TicketMovieResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.TheaterService;
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
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TheaterController {
    TheaterService theaterService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<TheaterResponse>> addNewTheater(@RequestBody @Valid TheaterNewRequest theaterNewRequest) {
        MessageResponse messageResponse = MessageResponse.THEATER_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<TheaterResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                        .data(theaterService.handleAddNewTheater(theaterNewRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<TheaterResponse>>> getTheaters() {
        MessageResponse messageResponse = MessageResponse.THEATER_GET_ALL_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<List<TheaterResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(theaterService.handleGetAllTheater())
                .build());
    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<TheaterResponse>> updateTheater(@PathVariable String id,@RequestBody @Valid TheaderUpdateRequeset theaterUpdateRequest) throws AppException {
//        MessageResponse messageResponse = MessageResponse.THEATER_UPDATE_SUCCESS;
//        return ResponseEntity.ok(ApiResponse.<TheaterResponse>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .data(ticketMovieService.handleUpdateTheater(id, theaterUpdateRequest))
//                .build());
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<Void>> deleteTheater(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.THEATER_DELETE_SUCCESS;
//        ticketMovieService.handleDeleteTheater(id);
//        return ResponseEntity.ok(ApiResponse.<Void>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
