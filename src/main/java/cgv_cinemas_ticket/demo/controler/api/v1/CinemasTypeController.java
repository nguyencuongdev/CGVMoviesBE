package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.admin.CinemasTypeNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasTypeResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.CinemasTypeService;
import cgv_cinemas_ticket.demo.service.FileService;
import cgv_cinemas_ticket.demo.service.PopcomService;
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
@RequestMapping("/api/v1/cinemas-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemasTypeController {
    CinemasTypeService cinemasTypeService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasTypeResponse>> addNewCinemasType(@RequestBody @Valid CinemasTypeNewOrUpdateRequest cinemasTypeNewRequest) {
        MessageResponse messageResponse = MessageResponse.CINEMASTYPE_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<CinemasTypeResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                        .data(cinemasTypeService.handleAddNewCinemasType(cinemasTypeNewRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<CinemasTypeResponse>>> getAllCinemasType() {
        MessageResponse messageResponse = MessageResponse.CINEMASTYPE_GET_ALL_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<List<CinemasTypeResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(cinemasTypeService.handleGetAllCinemasType())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasTypeResponse>> updateCinemasType(@PathVariable String id,@RequestBody @Valid CinemasTypeNewOrUpdateRequest cinemasTypeUpdateRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.CINEMASTYPE_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<CinemasTypeResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(cinemasTypeService.handleUpdateCinemasType(id, cinemasTypeUpdateRequest))
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<CinemasTypeResponse>> deleteCinemasType(@PathVariable String id) throws AppException {
        MessageResponse messageResponse = MessageResponse.CINEMASTYPE_DELETE_SUCCESS;
        cinemasTypeService.handleDeleteCinemasType(id);
        return ResponseEntity.ok(ApiResponse.<CinemasTypeResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .build());
    }
}
