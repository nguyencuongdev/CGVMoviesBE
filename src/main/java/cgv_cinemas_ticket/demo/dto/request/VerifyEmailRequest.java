package cgv_cinemas_ticket.demo.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VerifyEmailRequest {
    String email;
}
