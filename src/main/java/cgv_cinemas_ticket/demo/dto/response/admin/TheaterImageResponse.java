package cgv_cinemas_ticket.demo.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheaterImageResponse {
    Long id;
    String fileName;
    String srcImg;
}
