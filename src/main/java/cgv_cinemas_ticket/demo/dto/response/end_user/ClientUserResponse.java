package cgv_cinemas_ticket.demo.dto.response.end_user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientUserResponse {
    Long id;
    String name;
    String avatar;
    Date dateOfBirth;
    boolean gender;
    String faviousCinemas;
}
