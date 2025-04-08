package cgv_cinemas_ticket.demo.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationRequestParams {
    @Min(value = 0, message = "Page index must be >= 0")
    private Integer page = 0; // default là 0

    @Min(value = 1, message = "Page size must be >= 1")
    private Integer size = 20; // default là 10
}
