package cgv_cinemas_ticket.demo.dto.request.admin;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllCinemasFilterParams {
    String searchValue;
    Long theater_ID;
    String status;
    Long cinemas_type_ID;
}
