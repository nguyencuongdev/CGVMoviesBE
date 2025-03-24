package cgv_cinemas_ticket.demo.service;


import cgv_cinemas_ticket.demo.dto.request.DeleteMultiFileRequest;
import cgv_cinemas_ticket.demo.dto.request.FileInfoRequest;
import cgv_cinemas_ticket.demo.dto.response.FileContentResponse;
import cgv_cinemas_ticket.demo.dto.response.FileUploadResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.IFileMapper;
import cgv_cinemas_ticket.demo.model.FileTemp;
import cgv_cinemas_ticket.demo.repository.IFileTempRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileService {
    @Autowired
    IFileTempRepository fileTempRepository;
    @Autowired
    IFileMapper fileMapper;

    @Value("${file.upload-dir}")
    String uploadDir;

    @Value("${server.server-url}")
    String serverUrl;

    Path storageDirPath;

    @PostConstruct
    //annotation: này đánh dấu một method sẽ được gọi ngay sau khi bean được khởi tạo  injection hoàn tất.
    public void init() throws AppException {
        // set path folder storage
        storageDirPath = Paths.get(uploadDir).normalize().toAbsolutePath();
        // create folder not exist
        try {
            Files.createDirectories(storageDirPath);
        } catch (IOException e) {
            throw new AppException("Could not create storage directory!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    // Storage upload file to server and return file URL
    public FileUploadResponse storeFileUpload(MultipartFile fileUpload) throws AppException {
        String extension = FilenameUtils.getExtension(fileUpload.getOriginalFilename());
        String generatedFileName = randomFileName() + "." + extension;
        Path filePath = storageDirPath.resolve(generatedFileName).normalize().toAbsolutePath();

        try {
            if (!isValidFile(fileUpload, filePath)) {
                ErrorCode errorCode = ErrorCode.FILE_INVALID;
                throw new AppException(errorCode.getMessage(), errorCode.getCode());
            }
        } catch (IOException ex) {
            ErrorCode errorCode = ErrorCode.FILE_INVALID;
            throw new AppException(errorCode.getMessage(), errorCode.getCode());
        }

        try (InputStream inputStream = fileUpload.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AppException("Oh no! Storage file failed on server!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        String srcImg = serverUrl + "/api/v1/files/" + generatedFileName;
        FileTemp fileTemp = FileTemp.builder()
                .src(srcImg)
                .fileName(generatedFileName)
                .build();
        fileTemp = fileTempRepository.save(fileTemp);
        return fileMapper.toFileTempToFileUploadResponse(fileTempRepository.save(fileTemp));
    }

    public List<FileUploadResponse> handleStoreMultiFileUpload(MultipartFile[] files) throws AppException {
        List<FileUploadResponse> fileUploadResponses = new ArrayList<>();
        for (MultipartFile file : files) {
            FileUploadResponse fileUploadResponse = this.storeFileUpload(file);
            fileUploadResponses.add(fileUploadResponse);
        }
        return fileUploadResponses;
    }


    public FileContentResponse readFileContent(String fileName) throws AppException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize().toAbsolutePath();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                byte[] bytesContent = StreamUtils.copyToByteArray(resource.getInputStream());
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // MIME default
                }
                String srcImg = serverUrl + "/api/v1/files/" + fileName;
                return FileContentResponse.builder()
                        .content(bytesContent)
                        .contentType(contentType)
                        .fileName(fileName)
                        .srcImg(srcImg)
                        .build();
            } else {
                throw new AppException("File not exits!", HttpStatus.NOT_FOUND.value());
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("URI file invalid!");
        } catch (IOException ex) {
            throw new AppException("Read content file is failed!", HttpStatus.NOT_FOUND.value());
        }
    }

    public boolean deleteFileStoraged(String fileName) throws IOException {
        FileTemp fileTemp = fileTempRepository.findByFileName(fileName);
        if (Objects.nonNull(fileTemp)) {
            fileTempRepository.deleteById(fileTemp.getId());
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize().toAbsolutePath();
            return Files.deleteIfExists(filePath);
        }
        return false;
    }

    public List<Long> handleDeleteMultiFile(DeleteMultiFileRequest deleteMultiFileRequest) throws IOException {
        List<Long> fileDeletedIds = new ArrayList<>();
        for(FileInfoRequest file : deleteMultiFileRequest.getData()){
            boolean deleted = this.deleteFileStoraged(file.getFileName());
            if(deleted){
                fileDeletedIds.add(file.getId());
            }
        }
        return fileDeletedIds;
    }

    public boolean isValidFile(MultipartFile file, Path filePath) throws IOException {
        String[] fileTypesImageValid = new String[]{
                "image/png", "image/jpeg", "image/gif", "image/webp",
        };

        String[] fileTypesVideoValid = new String[]{
                "video/mp4", "video/x-msvideo", "video/quicktime", "video/x-ms-wmv", "video/webm"
        };
        String[] fileTypesAudioValid = new String[]{
                "audio/mpeg"
        };

        String fileType = Files.probeContentType(filePath);
        boolean checkValid = false;
        float fileSizeMB = file.getSize() / (1024.f * 1024.f); // convert bytes to MB

        if (Arrays.asList(fileTypesImageValid).contains(fileType)) {
            checkValid = fileSizeMB <= 5.0f;
        } else if (Arrays.asList(fileTypesVideoValid).contains(fileType)) {
            checkValid = fileSizeMB <= 100.0f;
        } else if (Arrays.asList(fileTypesAudioValid).contains(fileType)) {
            checkValid = fileSizeMB <= 50.0f;
        }

        return checkValid;
    }

    // Random fileName with length 12 characters
    public static String randomFileName() {
        final String CHARACTERISATION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int NAMELENGTH = 12;
        Random randomObject = new Random();
        StringBuilder fileNameRandom = new StringBuilder(NAMELENGTH);
        for (int i = 0; i < NAMELENGTH; i++) {
            fileNameRandom.append(
                    CHARACTERISATION.charAt(
                            randomObject.nextInt(CHARACTERISATION.length())
                    )
            );
        }
        return fileNameRandom.toString();
    }
}

