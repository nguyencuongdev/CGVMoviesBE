package cgv_cinemas_ticket.demo.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieCategoryResponse {
    Long id;
    String name;
    String description;
    boolean status;
    Date createAt;
    Date updateAt;
}
