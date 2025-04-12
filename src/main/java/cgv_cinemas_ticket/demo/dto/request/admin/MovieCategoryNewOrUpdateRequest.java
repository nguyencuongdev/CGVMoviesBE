package cgv_cinemas_ticket.demo.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCategoryNewOrUpdateRequest {
    @NotBlank(message = "required")
    @Size(min = 2, message = "movie-category-name-min-max", max = 50)
    String name;
    @Size(message = "movie-category-name-min-max", max = 255)
    String description;
    @NotNull(message="required")
    String status;
}
