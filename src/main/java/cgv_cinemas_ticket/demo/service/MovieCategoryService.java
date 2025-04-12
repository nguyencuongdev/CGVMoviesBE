package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllMovieCategoryFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.MovieCategoryNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.MovieCategoryResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.IMovieCategoryMapper;
import cgv_cinemas_ticket.demo.model.MovieCategory;
import cgv_cinemas_ticket.demo.repository.IMovieCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MovieCategoryService {
    IMovieCategoryRepository movieCategoryRepository;
    IMovieCategoryMapper movieCategoryMapper;

    public MovieCategoryResponse handleAddNewMovieCategory(MovieCategoryNewOrUpdateRequest movieCategoryRequest) {
        MovieCategory movieCategory = movieCategoryMapper.toMovieCategoryNewOrUpdateRequestToMovieCategory(movieCategoryRequest);
        movieCategory.setCreateAt(new Date());
        movieCategory.setUpdateAt(new Date());
        movieCategoryRepository.save(movieCategory);
        return movieCategoryMapper.toMovieCategoryToMovieCategoryResponse(movieCategory);
    }

    public DataListResponseWithPagination<List<MovieCategoryResponse>> handleGetAllMovieCategory(PaginationRequestParams paginationParams, GetAllMovieCategoryFilterParams filterParams) {
        int size = paginationParams.getSize();
        int page = paginationParams.getPage();

        List<MovieCategory> movieCategoriesMatchedFilterLimited = new ArrayList<>();
        List<MovieCategory> movieCategoriesMatchedFilter = new ArrayList<>();
        List<MovieCategory> movieCategoriesInDB = new ArrayList<>();

        if (Objects.nonNull(filterParams.getSearchValue())) {
            movieCategoriesInDB = movieCategoryRepository.findAllByNameContainingIgnoreCase(filterParams.getSearchValue()).stream().toList();
        } else {
            movieCategoriesInDB = movieCategoryRepository.findAll();
        }

        List<String> filterByFileds = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFileds.add("status");

        for (MovieCategory movieCategory : movieCategoriesInDB) {
            boolean checkMatch = isCheckMovieCategoryMatchFilter(filterParams, movieCategory, filterByFileds);
            if (checkMatch) {
                movieCategoriesMatchedFilter.add(movieCategory);
            }
        }

        int totalElements = movieCategoriesInDB.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());

//     Movie categories list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                movieCategoriesMatchedFilterLimited.add(movieCategoriesMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            movieCategoriesMatchedFilterLimited = movieCategoriesMatchedFilter;
        }
        List<MovieCategoryResponse> movieCategoriesResponse = movieCategoriesMatchedFilterLimited.stream().map(movieCategoryMapper::toMovieCategoryToMovieCategoryResponse).toList();
        return DataListResponseWithPagination.<List<MovieCategoryResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(movieCategoriesResponse)
                .build();
    }

    public MovieCategoryResponse handleUpdateMovieCategory(String id, MovieCategoryNewOrUpdateRequest movieCategoryRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.MOVIE_CATEGORY_NOT_EXISTED;
        MovieCategory movieCategory = movieCategoryRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));

        movieCategoryMapper.updateMovieCategory(movieCategory, movieCategoryRequest);
        movieCategory.setUpdateAt(new Date());
        movieCategoryRepository.save(movieCategory);
        return movieCategoryMapper.toMovieCategoryToMovieCategoryResponse(movieCategory);
    }

    //    public boolean handleDeleteMovieCategory(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.MOVIE_CATEGORY_NOT_EXISTED;
//        MovieCategory movieCategory = movieCategoryRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        movieCategoryRepository.deleteById(movieCategory.getId());
//        return true;
//    }
    private static boolean isCheckMovieCategoryMatchFilter(GetAllMovieCategoryFilterParams filterParams, MovieCategory cinemas, List<String> filterByFileds) {
        boolean checkMatch = true;
        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == cinemas.isStatus();
        }
        return checkMatch;
    }

}
