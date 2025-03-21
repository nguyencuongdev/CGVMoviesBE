package cgv_cinemas_ticket.demo.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheaterResponse {
    Long id;
    String name;
    String address;
    String city;
    boolean status;
    String note;
    String hotline;
    String fax;
    String linkMap;
    Date createAt;
    Date updateAt;
    Set<TheaterImageResponse> images;
}
