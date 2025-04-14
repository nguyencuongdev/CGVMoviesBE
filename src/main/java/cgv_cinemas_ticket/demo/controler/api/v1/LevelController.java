package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.admin.LevelNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.LevelResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.LevelService;
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
@RequestMapping("/api/v1/levels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class LevelController {
    LevelService levelService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<LevelResponse>> addNewLevel(@RequestBody @Valid LevelNewOrUpdateRequest levelNewRequest) {
        MessageResponse messageResponse = MessageResponse.LEVEL_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<LevelResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                        .data(levelService.handleAddNewLevel(levelNewRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<LevelResponse>>> getLevels() {
        MessageResponse messageResponse = MessageResponse.LEVEL_GET_ALL_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<List<LevelResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(levelService.handleGetAllLevel())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<LevelResponse>> updateLevel(@PathVariable String id,@RequestBody LevelNewOrUpdateRequest levelNewOrUpdateRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.LEVEL_GET_ALL_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<LevelResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(levelService.handleUpdateLevel(id, levelNewOrUpdateRequest))
                .build());
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<LevelResponse>> deleteLevel(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.LEVEL_DELETE_SUCCESS;
//        levelService.handleDeleteLevel(id);
//        return ResponseEntity.ok(ApiResponse.<LevelResponse>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
