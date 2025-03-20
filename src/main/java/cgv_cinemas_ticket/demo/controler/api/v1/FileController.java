package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.FileContentResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/files")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    // Endpoint upload file
    @PostMapping("/upload")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestParam("file") MultipartFile file) throws AppException {
        String fileURL = fileService.storeFileUpload(file);
        // Trả về URL để client xem file
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(false)
                        .statusCode(HttpStatus.OK.value())
                        .data(fileURL)
                        .message("upload-file-success!")
                        .build()
        );
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) throws AppException, RuntimeException {
        FileContentResponse fileContentResponse = fileService.readFileContent(fileName);
        return ResponseEntity.ok()
                .contentType(
                        MediaType.valueOf(fileContentResponse.getContentType())
                )
                .body(fileContentResponse.getContent());
    }

    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    @DeleteMapping("/{fileName:.+}")
    public ResponseEntity<ApiResponse<Object>> deleteFile(@PathVariable String fileName) throws AppException {
        ErrorCode errorCode = ErrorCode.FILE_NOT_FOUND;
        try {
            boolean deleteFileStatus = fileService.deleteFileStoraged(fileName);
            return deleteFileStatus ? ResponseEntity.ok()
                    .body(
                            ApiResponse.builder()
                                    .status(true)
                                    .statusCode(HttpStatus.OK.value())
                                    .message("Delete file with name: " + fileName + " successfully!")
                                    .build()
                    ) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(
                                    ApiResponse.builder()
                                            .status(false)
                                            .statusCode(errorCode.getCode())
                                            .message(errorCode.getMessage())
                                            .build()
                            )
                    ;

        } catch (IOException ex) {
            throw new AppException(errorCode.getMessage(), errorCode.getCode());
        }
    }
}
