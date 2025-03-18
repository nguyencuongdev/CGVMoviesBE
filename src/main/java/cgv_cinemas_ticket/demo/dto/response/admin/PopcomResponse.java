package cgv_cinemas_ticket.demo.dto.response.admin;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopcomResponse {
    Long id;
    String name;
    String srcImg;
    int price;
}
