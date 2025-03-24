package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.admin.TheaterNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterImageResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ITheaterMapper;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.model.TheaterImage;
import cgv_cinemas_ticket.demo.repository.IFileTempRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterImageRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TheaterService {
    ITheaterRepository theaterRepository;
    ITheaterImageRepository theaterImageRepository;
    IFileTempRepository fileTempRepository;
    ITheaterMapper theaterMapper;
    private final FileService fileService;

    public TheaterResponse handleAddNewTheater(TheaterNewOrUpdateRequest theaterNewRequest) {
        Theater theater = theaterMapper.toTheaterNewRequestToTheater(theaterNewRequest);
        Set<TheaterImage> theaterImageList = new HashSet<>();
        theaterNewRequest.getImages().forEach(fileImage -> {
            TheaterImage theaterImage = TheaterImage.builder()
                    .srcImg(fileImage.getSrc())
                    .fileName(fileImage.getFileName())
                    .theater(theater)
                    .build();
            theaterImageList.add(theaterImage);
        });
        theater.setStatus(true);
        theater.setCreateAt(new Date());
        theater.setUpdateAt(new Date());
        theaterRepository.save(theater);
        Set<TheaterImageResponse> theaterImageResponseList = theaterImageRepository.saveAll(theaterImageList)
                .stream()
                .map(theaterMapper::toTheaterImageToTheaterImageResponse)
                .collect(Collectors.toSet());
        TheaterResponse theaterResponse = theaterMapper.toTheaterToTheaterResponse(theater);
        theaterResponse.setImages(theaterImageResponseList);
        return theaterMapper.toTheaterToTheaterResponse(theater);
    }

    public List<TheaterResponse> handleGetAllTheater() {
        List<Theater> theaterList = theaterRepository.findAll();
        return theaterList.stream().map(theaterMapper::toTheaterToTheaterResponse).toList();
    }

    //
    public TheaterResponse handleUpdateTheater(String id, TheaterNewOrUpdateRequest theaterUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.THEATER_NOT_EXISTED;
        Theater theater = theaterRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));

        // delete info old theater image list
        Set<Long> theaterImageOldIds = new HashSet<>();
        theater.getImages().forEach(fileImage -> {
            try {
                fileService.deleteFileStoraged(fileImage.getFileName());
            } catch (Exception ex) {
                log.error("Delete file with fileName: {} get a error: {}", fileImage.getFileName(), ex.getMessage());
            }
        });
        theaterImageRepository.deleteAllById(theaterImageOldIds);

        // set theater image list new for theater
        Set<TheaterImage> theaterImageList = new HashSet<>();
        theaterUpdateRequest.getImages().forEach(fileImage -> {
            TheaterImage theaterImage = TheaterImage.builder()
                    .srcImg(fileImage.getSrc())
                    .fileName(fileImage.getFileName())
                    .theater(theater)
                    .build();
            theaterImageList.add(theaterImage);
        });
        Set<TheaterImageResponse> theaterImageResponseList = theaterImageRepository.saveAll(theaterImageList)
                .stream()
                .map(theaterMapper::toTheaterImageToTheaterImageResponse)
                .collect(Collectors.toSet());

        theater.setStatus(true);
        theater.setUpdateAt(new Date());
        theaterMapper.updateTheater(theater, theaterUpdateRequest);
        theaterRepository.save(theater);

        TheaterResponse theaterResponse = theaterMapper.toTheaterToTheaterResponse(theater);
        theaterResponse.setImages(theaterImageResponseList);
        return theaterResponse;
    }
    public void handleDeleteTheater(String id) throws AppException {
        ErrorCode errorCode = ErrorCode.THEATER_NOT_EXISTED;
        Theater theater = theaterRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));
        Set<Long> theaterImageIds = new HashSet<>();
        theater.getImages().forEach(fileImage -> {
            try {
                fileService.deleteFileStoraged(fileImage.getFileName());
            } catch (Exception ex) {
                log.error("Delete file with fileName: {} get a error: {}", fileImage.getFileName(), ex.getMessage());
            }
        });
        theaterImageRepository.deleteAllById(theaterImageIds);
        theaterRepository.deleteById(theater.getId());
    }
}
