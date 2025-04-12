package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.CinemasNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllCinemasFilterParams;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.ValidationExceptionResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ICinemasMapper;
import cgv_cinemas_ticket.demo.model.Cinemas;
import cgv_cinemas_ticket.demo.model.CinemasType;
import cgv_cinemas_ticket.demo.model.Theater;
import cgv_cinemas_ticket.demo.repository.ICinemasRepository;
import cgv_cinemas_ticket.demo.repository.ICinemasTypeRepository;
import cgv_cinemas_ticket.demo.repository.ITheaterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CinemasService {
    ICinemasMapper cinemasMapper;
    ICinemasRepository cinemasRepository;
    ITheaterRepository theaterRepository;
    ICinemasTypeRepository cinemasTypeRepository;

    public CinemasResponse handleAddNewCinemas(CinemasNewOrUpdateRequest cinemasNewRequest) throws ValidationExceptionResponse {
        Map<String, String> errors = new HashMap<>();
        Optional<Theater> theater = theaterRepository.findById(cinemasNewRequest.getTheater_ID());
        if (theater.isEmpty()) {
            errors.put("theater_ID", "Theater not existed!");
        }
        Optional<CinemasType> cinemasType = cinemasTypeRepository.findById(cinemasNewRequest.getCinemasType_ID());
        if (cinemasType.isEmpty()) {
            errors.put("cinemas_type_ID", "Cinemas type not existed!");
        }

        if (!errors.isEmpty()) throw ValidationExceptionResponse.builder()
                .status(false)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Add new a cinemas failed!")
                .errors(errors)
                .build();

        Cinemas cinemas = cinemasMapper.toCinemasNewOrUpdateRequestToCinemas(cinemasNewRequest);
        cinemas.setCinemasType(cinemasType.get());
        cinemas.setTheater(theater.get());
        cinemas.setCreateAt(new Date());
        cinemas.setUpdateAt(new Date());
        cinemasRepository.save(cinemas);
        return cinemasMapper.toCinemasToCinemasResponse(cinemas);
    }

    public DataListResponseWithPagination<List<CinemasResponse>> handleGetAllCinemasOfTheater(String theaterID, PaginationRequestParams paginationParams) throws AppException {
        Theater theater = theaterRepository.findById(Long.parseLong(theaterID)).orElseThrow(
                () -> new AppException("theater-not-exist", HttpStatus.NOT_FOUND.value())
        );
        Pageable pagination = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        Page<Cinemas> cinemasPage = cinemasRepository.findAllByTheater(theater, pagination);
        int totalCinemasInDB = cinemasRepository.countByTheater(theater);
        int totalPages = (int) Math.ceil((double) totalCinemasInDB / paginationParams.getSize());
        List<CinemasResponse> cinemasResponseList = cinemasPage.stream().map(cinemasMapper::toCinemasToCinemasResponse).toList();
        return DataListResponseWithPagination.<List<CinemasResponse>>builder()
                .page(paginationParams.getPage())
                .size(paginationParams.getSize())
                .totalPages(totalPages)
                .totalElements(totalCinemasInDB)
                .data(cinemasResponseList)
                .build();
    }

    public DataListResponseWithPagination<List<CinemasResponse>> handleGetAllCinemas(PaginationRequestParams paginationParams, GetAllCinemasFilterParams filterParams) throws AppException {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<Cinemas> cinemasListMatchedFilterLimited = new ArrayList<>();
        List<Cinemas> cinemasListMatchedFilter = new ArrayList<>();
        List<Cinemas> cinemasListInDB = new ArrayList<>();
        if (Objects.isNull(filterParams.getSearchValue())) {
            cinemasListInDB = cinemasRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            cinemasListInDB = cinemasRepository.findByNameContainingIgnoreCaseOrTheater_NameContainingIgnoreCaseOrCinemasType_NameContainingIgnoreCase(searchValue, searchValue, searchValue);
        }
        List<String> filterByFileds = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFileds.add("status");
        if (Objects.nonNull(filterParams.getCinemas_type_ID())) filterByFileds.add("cinemas_type");
        if (Objects.nonNull(filterParams.getTheater_ID())) filterByFileds.add("theater");


        for (Cinemas cinemas : cinemasListInDB) {
            boolean checkMatch = isCheckCinemasMatchFilter(filterParams, cinemas, filterByFileds);
            if (checkMatch) {
                cinemasListMatchedFilter.add(cinemas);
            }
        }
        int totalElements = cinemasListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());

//           Cinemas list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                cinemasListMatchedFilterLimited.add(cinemasListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            cinemasListMatchedFilterLimited = cinemasListMatchedFilter;
        }
        List<CinemasResponse> cinemasResponseList = cinemasListMatchedFilterLimited.stream().map(cinemasMapper::toCinemasToCinemasResponse).toList();
        return DataListResponseWithPagination.<List<CinemasResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(cinemasResponseList)
                .build();
    }

    private static boolean isCheckCinemasMatchFilter(GetAllCinemasFilterParams filterParams, Cinemas cinemas, List<String> filterByFileds) {
        boolean checkMatch = true;
        if (filterByFileds.contains("theater") && !Objects.equals(filterParams.getTheater_ID(), cinemas.getTheater().getId())) {
            checkMatch = false;
        }
        if (filterByFileds.contains("cinemas_type") && !Objects.equals(filterParams.getCinemas_type_ID(), cinemas.getCinemasType().getId())) {
            checkMatch = false;
        }

        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == cinemas.isStatus();
        }
        return checkMatch;
    }


    public CinemasResponse handleUpdateCinemas(String id, CinemasNewOrUpdateRequest cinemasUpdateRequest) throws AppException, ValidationExceptionResponse {
        Map<String, String> errors = new HashMap<>();
        Optional<Theater> theater = theaterRepository.findById(cinemasUpdateRequest.getTheater_ID());
        if (theater.isEmpty()) {
            errors.put("theater_ID", "Theater not existed!");
        }
        Optional<CinemasType> cinemasType = cinemasTypeRepository.findById(cinemasUpdateRequest.getCinemasType_ID());
        if (cinemasType.isEmpty()) {
            errors.put("cinemas_type_ID", "Cinemas type not existed!");
        }

        if (!errors.isEmpty()) throw ValidationExceptionResponse.builder()
                .status(false)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Add new a cinemas failed!")
                .errors(errors)
                .build();
        ErrorCode errorCode = ErrorCode.CINEMAS_NOT_EXISTED;
        Cinemas cinemas = cinemasRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));
        cinemasMapper.updateCinemas(cinemas, cinemasUpdateRequest);
        cinemas.setUpdateAt(new Date());
        cinemas.setTheater(theater.get());
        cinemas.setCinemasType(cinemasType.get());
        cinemasRepository.save(cinemas);
        return cinemasMapper.toCinemasToCinemasResponse(cinemas);
    }


    public boolean handleDeleteCinemas(String id) throws AppException {
        ErrorCode errorCode = ErrorCode.CINEMAS_NOT_EXISTED;
        Cinemas cinemas = cinemasRepository.findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));
        cinemasRepository.deleteById(cinemas.getId());
        return true;
    }
}
