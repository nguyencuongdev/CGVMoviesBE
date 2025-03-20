package cgv_cinemas_ticket.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketMovieUpdateRequest {
    @Size(min = 2, max = 255, message = "ticket-movie-name-invalid")
    String name;
    int price;
    String note;
}
