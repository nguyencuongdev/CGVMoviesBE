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
public class CinemasResponse {
    Long id;
    String name;
    boolean status;
    int standardChairs;
    int vipChairs;
    int sweetboxChairs;
    CinemasTypeResponse cinemasType;
    TheaterResponse theater;
    Date createAt;
    Date updateAt;
}
