package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.CinemasNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.ValidationExceptionResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.CinemasService;
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
@RequestMapping("/api/v1/cinemas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemasController {
    CinemasService cinemasService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasResponse>> addNewCinemas(@RequestBody @Valid CinemasNewOrUpdateRequest cinemasNewRequest) throws ValidationExceptionResponse {
        MessageResponse messageResponse = MessageResponse.CINEMAS_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<CinemasResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(cinemasService.handleAddNewCinemas(cinemasNewRequest))
                .build()
        );
    }

    @GetMapping("/by-theater/{theaterID}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<CinemasResponse>>> getAllCinemasByTheater(@PathVariable String theaterID, @ModelAttribute @Valid PaginationRequestParams paginationParams) throws AppException {
        MessageResponse messageResponse = MessageResponse.CINEMAS_GET_ALL_SUCCESS;
        DataListResponseWithPagination<List<CinemasResponse>> dataCinemasResponseList = cinemasService.handleGetAllCinemasOfTheater(theaterID, paginationParams);
        return ResponseEntity.ok(ApiResponse.<List<CinemasResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .size(paginationParams.getSize())
                .page(paginationParams.getPage())
                .totalElements(dataCinemasResponseList.getTotalElements())
                .totalPages(dataCinemasResponseList.getTotalPages())
                .data(dataCinemasResponseList.getData())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasResponse>> updateCinemas(@PathVariable String id, @RequestBody @Valid CinemasNewOrUpdateRequest cinemasUpdateRequest) throws AppException, ValidationExceptionResponse {
        MessageResponse messageResponse = MessageResponse.CINEMAS_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<CinemasResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(cinemasService.handleUpdateCinemas(id, cinemasUpdateRequest))
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasResponse>> deleteCinemas(@PathVariable String id) throws AppException {
        MessageResponse messageResponse = MessageResponse.CINEMAS_DELETE_SUCCESS;
        cinemasService.handleDeleteCinemas(id);
        return ResponseEntity.ok(ApiResponse.<CinemasResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .build());
    }
}
