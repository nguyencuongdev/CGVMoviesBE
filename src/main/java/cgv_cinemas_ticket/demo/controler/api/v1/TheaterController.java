package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllTheaterFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.TheaterNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ValidationException;
import cgv_cinemas_ticket.demo.service.TheaterService;
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
    ResponseEntity<ApiResponse<TheaterResponse>> addNewTheater(@RequestBody @Valid TheaterNewOrUpdateRequest theaterNewRequest) throws ValidationException {
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
    ResponseEntity<ApiResponse<List<TheaterResponse>>> getTheaters(@ModelAttribute @Valid PaginationRequestParams paginationParams, @ModelAttribute @Valid GetAllTheaterFilterParams filterParams) {
        MessageResponse messageResponse = MessageResponse.THEATER_GET_ALL_SUCCESS;
        DataListResponseWithPagination<List<TheaterResponse>> dataTheaterList = theaterService.handleGetAllTheater(paginationParams, filterParams);
        return ResponseEntity.ok(ApiResponse.<List<TheaterResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .page(dataTheaterList.getPage())
                .totalPages(dataTheaterList.getTotalPages())
                .totalElements(dataTheaterList.getTotalElements())
                .size(dataTheaterList.getSize())
                .data(dataTheaterList.getData())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<TheaterResponse>> updateTheater(@PathVariable String id, @RequestBody @Valid TheaterNewOrUpdateRequest theaterUpdateRequest) throws AppException, ValidationException {
        MessageResponse messageResponse = MessageResponse.THEATER_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<TheaterResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(theaterService.handleUpdateTheater(id, theaterUpdateRequest))
                .build());
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<Void>> deleteTheater(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.THEATER_DELETE_SUCCESS;
//        theaterService.handleDeleteTheater(id);
//        return ResponseEntity.ok(ApiResponse.<Void>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
