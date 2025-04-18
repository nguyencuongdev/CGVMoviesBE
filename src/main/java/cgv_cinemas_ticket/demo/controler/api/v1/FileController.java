package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.dto.request.DeleteMultiFileRequest;
import cgv_cinemas_ticket.demo.dto.request.FileInfoRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.FileContentResponse;
import cgv_cinemas_ticket.demo.dto.response.FileUploadResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/files")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    // Endpoint upload single file
    @PostMapping("/upload")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(@RequestParam("file") MultipartFile file) throws AppException {
        FileUploadResponse fileUploadResponse = fileService.storeFileUpload(file);
        // Trả về Info file uploaded on the server: id, src, fileName
        return ResponseEntity.ok(
                ApiResponse.<FileUploadResponse>builder()
                        .status(false)
                        .statusCode(HttpStatus.OK.value())
                        .data(fileUploadResponse)
                        .message("upload-file-success!")
                        .build()
        );
    }

    //    Endpoint upload multi file
    @PostMapping("/uploads")
    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadMultiFile(@RequestParam("files") MultipartFile[] files) throws AppException {
        List<FileUploadResponse> fileUploadResponseList = fileService.handleStoreMultiFileUpload(files);
//         Trả về list info file uploaded on the server: id, src, fileName
        return ResponseEntity.ok(
                ApiResponse.<List<FileUploadResponse>>builder()
                        .status(true)
                        .statusCode(HttpStatus.OK.value())
                        .data(fileUploadResponseList)
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

    @PreAuthorize("hasRole('CONTENT_MANAGER')")
    @DeleteMapping()
    public ResponseEntity<ApiResponse<List<Long>>> deleteMultiFile(@RequestBody DeleteMultiFileRequest deleteMultiFileRequest) throws AppException, IOException {
        List<Long> fileDeletedIds = fileService.handleDeleteMultiFile(deleteMultiFileRequest);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<Long>>builder()
                                .status(true)
                                .statusCode(HttpStatus.OK.value())
                                .message("Delete files successfully!")
                                .data(fileDeletedIds)
                                .build()
                );
    }
}
