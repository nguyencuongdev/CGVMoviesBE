package cgv_cinemas_ticket.demo.dto.request.admin;

import cgv_cinemas_ticket.demo.dto.request.FileInfoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TheaterNewOrUpdateRequest {
    @NotBlank(message = "required")
    @Size(min = 2, max = 255, message = "theater-name-invalid")
    String name;
    @NotBlank(message = "required")
    @Size(min = 2, max = 255, message = "theater-address-invalid")
    String address;
    @NotBlank(message = "required")
    @Size(min = 2, max = 255, message = "theater-city-invalid")
    private String city;
    @NotNull(message = "required")
    @Size(max = 10, message = "theater-name-invalid")
    String hotline;
    @NotNull(message = "required")
    @Size(min = 2, max = 255, message = "theater-name-invalid")
    String fax;
    @NotBlank(message = "required")
    @Size(min = 2, max = 255, message = "theater-name-invalid")
    String linkMap;
    @NotNull(message = "required")
    List<FileInfoRequest> images;
    String note;
}
