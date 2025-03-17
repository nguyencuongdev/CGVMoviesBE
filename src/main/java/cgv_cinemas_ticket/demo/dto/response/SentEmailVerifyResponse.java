package cgv_cinemas_ticket.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SentEmailVerifyResponse {
    private boolean status;
    private String message;
}
