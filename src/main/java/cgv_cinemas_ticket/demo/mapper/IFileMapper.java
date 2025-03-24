package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.response.FileUploadResponse;
import cgv_cinemas_ticket.demo.model.FileTemp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFileMapper {
    FileUploadResponse toFileTempToFileUploadResponse(FileTemp fileTemp);
//    void updateTicketMovie(@MappingTarget Theater theater, TheaterUpdateRequest theaterUpdateRequest);
}
