package cgv_cinemas_ticket.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketMovieResponse {
    Long id;
    String name;
    int price;
    String note;
}
