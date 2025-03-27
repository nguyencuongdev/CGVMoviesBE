package cgv_cinemas_ticket.demo.dto.request.admin;

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
public class CinemasNewOrUpdateRequest {
    @NotNull(message = "required")
    @Size(min = 2, max = 255, message = "cinemas-type-name-invalid")
    String name;
    @NotNull(message = "required")
    boolean status;
    @NotNull(message = "required")
    int standardChairs;
    @NotNull(message = "required")
    int vipChairs;
    @NotNull(message = "required")
    int sweetboxChairs;
    @NotNull(message = "required")
    Long cinemasType_ID;
    @NotNull(message = "required")
    Long theater_ID;
}
