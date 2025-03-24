package cgv_cinemas_ticket.demo.dto.request.admin;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelNewOrUpdateRequest {
    @NotBlank(message = "NAME_REQUIRED")
    @Size(min = 2, max = 255, message = "NAME_SIZE_RANGE")
    String name;
    @NotNull(message = "POINT_REQUIRED")
    int point;
    String description;
}
