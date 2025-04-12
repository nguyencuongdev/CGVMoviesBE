package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllMovieCategoryFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.MovieCategoryNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.MovieCategoryResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.MovieCategoryService;
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
@RequestMapping("/api/v1/movie-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieCategoryController {
    MovieCategoryService movieCategoryServiceService;

    @PostMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<MovieCategoryResponse>> addNewMovieCategories(@RequestBody @Valid MovieCategoryNewOrUpdateRequest movieCategoryRequest) {
        MessageResponse messageResponse = MessageResponse.MOVIE_CATEGORY_ADD_NEW_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<MovieCategoryResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(MovieCategoryController.this.movieCategoryServiceService.handleAddNewMovieCategory(movieCategoryRequest))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<List<MovieCategoryResponse>>> getMovieCategories(@ModelAttribute @Valid PaginationRequestParams paginationParams, @ModelAttribute @Valid GetAllMovieCategoryFilterParams filterParams) {
        MessageResponse messageResponse = MessageResponse.MOVIE_CATEGORY_GET_ALL_SUCCESS;
        DataListResponseWithPagination<List<MovieCategoryResponse>> dataMovieCategoryResponseList = movieCategoryServiceService.handleGetAllMovieCategory(paginationParams,filterParams);
        return ResponseEntity.ok(ApiResponse.<List<MovieCategoryResponse>>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .size(paginationParams.getSize())
                .page(paginationParams.getPage())
                .totalElements(dataMovieCategoryResponseList.getTotalElements())
                .totalPages(dataMovieCategoryResponseList.getTotalPages())
                .data(dataMovieCategoryResponseList.getData())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    ResponseEntity<ApiResponse<MovieCategoryResponse>> updateMovieCategories(@PathVariable String id, @RequestBody @Valid MovieCategoryNewOrUpdateRequest movieCategoryRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.MOVIE_CATEGORY_UPDATE_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<MovieCategoryResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(movieCategoryServiceService.handleUpdateMovieCategory(id, movieCategoryRequest))
                .build());
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('CONTENT_MANAGER')")
//    ResponseEntity<ApiResponse<MovieCategoryResponse>> deleteMovieCategories(@PathVariable String id) throws AppException {
//        MessageResponse messageResponse = MessageResponse.MOVIE_CATEGORY_DELETE_SUCCESS;
//        MovieCategoryController.this.movieCategoryServiceService.handleDeleteMovieCategories(id);
//        return ResponseEntity.ok(ApiResponse.<MovieCategoryResponse>builder()
//                .status(true)
//                .statusCode(HttpStatus.OK.value())
//                .message(messageResponse.getMessage())
//                .build());
//    }
}
