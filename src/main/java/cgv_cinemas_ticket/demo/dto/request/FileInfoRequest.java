package cgv_cinemas_ticket.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileInfoRequest {
   Long id;
   String src;
   String fileName;
}
