package cgv_cinemas_ticket.demo.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class FileContentResponse {
    String id;
    byte[] content;
    String fileName;
    String srcImg;
    String contentType;
}
