package cgv_cinemas_ticket.demo.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class FileUploadResponse {
    String id;
    String fileName;
    String src;
}
