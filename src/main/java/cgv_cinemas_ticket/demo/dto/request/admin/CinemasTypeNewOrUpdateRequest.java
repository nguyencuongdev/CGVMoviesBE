package cgv_cinemas_ticket.demo.dto.request.admin;

import cgv_cinemas_ticket.demo.dto.request.FileInfoRequest;
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
public class CinemasTypeNewOrUpdateRequest {
    @NotNull(message = "required")
    @Size(min = 2, max = 255, message = "cinemas-type-name-invalid")
    String name;
    @NotNull(message = "required")
    String srcAvatar;
    String note;
}
