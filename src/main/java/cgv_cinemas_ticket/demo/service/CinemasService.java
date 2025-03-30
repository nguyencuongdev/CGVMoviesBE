package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.CinemasNewOrUpdateRequest;
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
