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
public class TicketMovieNewRequest {
    @NotBlank(message = "required")
    @Size(min = 2, max = 255, message = "ticket-movie-name-invalid")
    String name;
    @NotNull(message = "required")
    int price;
    String note;
}
