package cgv_cinemas_ticket.demo.dto.response.admin;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemasTypeResponse {
    Long id;
    String name;
    String avatar;
    String note;
    Date createAt;
    Date updateAt;
}
