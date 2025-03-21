package cgv_cinemas_ticket.demo.mapper;


import cgv_cinemas_ticket.demo.dto.request.TheaterNewRequest;
import cgv_cinemas_ticket.demo.dto.response.FileUploadResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.model.FileTemp;
import cgv_cinemas_ticket.demo.model.Theater;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IFileMapper {
    FileUploadResponse toFileTempToFileUploadResponse(FileTemp fileTemp);
//    void updateTicketMovie(@MappingTarget Theater theater, TheaterUpdateRequest theaterUpdateRequest);
}
