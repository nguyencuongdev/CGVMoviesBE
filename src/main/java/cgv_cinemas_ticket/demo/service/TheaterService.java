package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllTheaterFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.TheaterNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.TicketMovieResponse;
import cgv_cinemas_ticket.demo.exception.ValidationException;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterImageResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TheaterResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ITheaterMapper;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.model.TheaterImage;
//import cgv_cinemas_ticket.demo.repository.IFileTempRepository;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import cgv_cinemas_ticket.demo.repository.ITheaterImageRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TheaterService {
    ITheaterRepository theaterRepository;
    ITheaterImageRepository theaterImageRepository;
    //    IFileTempRepository fileTempRepository;
    ITheaterMapper theaterMapper;
    private final FileService fileService;

    public TheaterResponse handleAddNewTheater(TheaterNewOrUpdateRequest theaterNewRequest) throws ValidationException {
        Theater theater = theaterMapper.toTheaterNewRequestToTheater(theaterNewRequest);
        Map<String, String> errors = validateDuplicate(theater, "add");
        if (!errors.isEmpty()) {
            ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE;
            throw new ValidationException(errorCode.getMessage(), errorCode.getStatusCode().value(), errors);
        }
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

    public DataListResponseWithPagination<List<TheaterResponse>> handleGetAllTheater(PaginationRequestParams paginationParams, GetAllTheaterFilterParams filterParams) {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<Theater> theaterListMatchedFilterLimited = new ArrayList<>();
        List<Theater> theaterListMatchedFilter = new ArrayList<>();
        List<Theater> theaterListInDB = new ArrayList<>();

        if (Objects.isNull(filterParams.getSearchValue())) {
            theaterListInDB = theaterRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            theaterListInDB = theaterRepository.findAllByNameContainingIgnoreCase(searchValue);
        }
        List<String> filterByFields = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFields.add("status");
        if (Objects.nonNull(filterParams.getCity())) filterByFields.add("city");

        for (Theater theater : theaterListInDB) {
            boolean checkMatch = isCheckTheaterMatchFilter(filterParams, theater, filterByFields);
            if (checkMatch) {
                theaterListMatchedFilter.add(theater);
            }
        }
        int totalElements = theaterListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());

        // Theater list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                theaterListMatchedFilterLimited.add(theaterListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            theaterListMatchedFilterLimited = theaterListMatchedFilter;
        }

        List<TheaterResponse> theaterResponseList = theaterListMatchedFilterLimited.stream().map(theaterMapper::toTheaterToTheaterResponse).toList();
        return DataListResponseWithPagination.<List<TheaterResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(theaterResponseList)
                .build();
    }

    //
    public TheaterResponse handleUpdateTheater(String id, TheaterNewOrUpdateRequest theaterUpdateRequest) throws AppException, ValidationException {
        ErrorCode errorCode = ErrorCode.THEATER_NOT_EXISTED;
        Theater theater = theaterRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));

        Theater theaterUpdate = theaterMapper.toTheaterNewRequestToTheater(theaterUpdateRequest);
        theaterUpdate.setId(theater.getId());
        Map<String, String> errors = validateDuplicate(theaterUpdate, "update");
        if (!errors.isEmpty()) {
            ErrorCode errorCode2 = ErrorCode.DUPLICATE_RESOURCE;
            throw new ValidationException(errorCode2.getMessage(), errorCode2.getStatusCode().value(), errors);
        }

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

    private Map<String, String> validateDuplicate(Theater theater, String type) {
        Map<String, String> errorMap = new HashMap<>();
        String name = theater.getName();
        String address = theater.getAddress();
        if (Objects.equals(type, "add")) {
            if (theaterRepository.existsByName(name))
                errorMap.put("name", "Theater name already exists");
            if (theaterRepository.existsByAddress(address))
                errorMap.put("address", "Theater address already exists");
        } else if (Objects.equals(type, "update")) {
            Long theaterId = theater.getId();
            if (theaterRepository.existsByNameAndIdNot(name, theaterId))
                errorMap.put("name", "Theater name already exists");
            if (theaterRepository.existsByAddressAndIdNot(address, theaterId))
                errorMap.put("address", "Theater address already exists");
        }
        return errorMap;
    }
    private static boolean isCheckTheaterMatchFilter(GetAllTheaterFilterParams filterParams, Theater theater, List<String> filterByFileds) {
        boolean checkMatch = true;
        if(filterByFileds.contains("city") && !Objects.equals(filterParams.getCity(), theater.getCity())){
            checkMatch = false;
        }
        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == theater.isStatus();
        }
        return checkMatch;
    }
//    public void handleDeleteTheater(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.THEATER_NOT_EXISTED;
//        Theater theater = theaterRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));
//        Set<Long> theaterImageIds = new HashSet<>();
//        theater.getImages().forEach(fileImage -> {
//            try {
//                fileService.deleteFileStoraged(fileImage.getFileName());
//            } catch (Exception ex) {
//                log.error("Delete file with fileName: {} get a error: {}", fileImage.getFileName(), ex.getMessage());
//            }
//        });
//        theaterImageRepository.deleteAllById(theaterImageIds);
//        theaterRepository.deleteById(theater.getId());
//    }
}
