package cgv_cinemas_ticket.demo.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class RefreshTokenResponse {
    private String token;
}
