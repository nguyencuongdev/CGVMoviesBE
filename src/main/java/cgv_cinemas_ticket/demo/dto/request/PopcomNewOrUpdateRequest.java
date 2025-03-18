package cgv_cinemas_ticket.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopcomNewOrUpdateRequest {
    String name;
    String srcImg;
    int price;
}
