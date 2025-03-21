package cgv_cinemas_ticket.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteMultiFileRequest {
  FileInfoRequest[] data;
}
