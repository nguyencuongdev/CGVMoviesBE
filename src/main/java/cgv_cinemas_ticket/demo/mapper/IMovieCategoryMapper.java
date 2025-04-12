package cgv_cinemas_ticket.demo.mapper;

import cgv_cinemas_ticket.demo.dto.request.admin.MovieCategoryNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.MovieCategoryResponse;
import cgv_cinemas_ticket.demo.model.MovieCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IMovieCategoryMapper {
    MovieCategory toMovieCategoryNewOrUpdateRequestToMovieCategory(MovieCategoryNewOrUpdateRequest movieCategoryNewOrUpdateRequest);
    MovieCategoryResponse toMovieCategoryToMovieCategoryResponse(MovieCategory movieCategory);
    void updateMovieCategory(@MappingTarget MovieCategory movieCategory, MovieCategoryNewOrUpdateRequest movieCategoryUpdateRequest);
}
