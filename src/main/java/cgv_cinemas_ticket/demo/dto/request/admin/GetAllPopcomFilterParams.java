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
public class GetAllPopcomFilterParams {
    String searchValue;
    String status;
}
