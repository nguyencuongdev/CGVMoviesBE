package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllPopcomFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
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
@RequestMapping("/api/v1/popcoms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopcomController {
    PopcomService popcomService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<PopcomResponse>> addNewPopcom(@RequestBody @Valid PopcomNewOrUpdateRequest popcomNewRequest) {
        MessageResponse messageResponse = MessageResponse.POPCOM_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<PopcomResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                        .data(popcomService.handleAddNewPopcom(popcomNewRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<PopcomResponse>>> getPopcoms(@ModelAttribute @Valid PaginationRequestParams paginationParams, @ModelAttribute @Valid GetAllPopcomFilterParams filterParams) {
        MessageResponse messageResponse = MessageResponse.POPCOM_GET_ALL_SUCCESS;
        DataListResponseWithPagination<List<PopcomResponse>> dataPopcomResponseList = popcomService.handleGetAllPopcom(paginationParams, filterParams);
        return ResponseEntity.ok(ApiResponse.<List<PopcomResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .size(paginationParams.getSize())
                .page(paginationParams.getPage())
                .totalElements(dataPopcomResponseList.getTotalElements())
                .totalPages(dataPopcomResponseList.getTotalPages())
                .data(dataPopcomResponseList.getData())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<PopcomResponse>> updatePopcom(@PathVariable String id,@RequestBody @Valid PopcomNewOrUpdateRequest popcomNewRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.POPCOM_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<PopcomResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(popcomService.handleUpdatePopcom(id, popcomNewRequest))
                .build());
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<PopcomResponse>> deletePopcom(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.POPCOM_DELETE_SUCCESS;
//        popcomService.handleDeletePopcom(id);
//        return ResponseEntity.ok(ApiResponse.<PopcomResponse>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
