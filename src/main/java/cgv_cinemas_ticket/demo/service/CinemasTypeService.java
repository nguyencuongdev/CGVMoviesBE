package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.CinemasTypeNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllCinemasTypeFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllTicketMovieFilterParams;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasTypeResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.TicketMovieResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ICinemasTypeMapper;
import cgv_cinemas_ticket.demo.model.CinemasType;
import cgv_cinemas_ticket.demo.model.TicketMovie;
import cgv_cinemas_ticket.demo.repository.ICinemasTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CinemasTypeService {
    ICinemasTypeMapper cinemasTypeMapper;
    ICinemasTypeRepository cinemasTypeRepository;
    FileService fileService;

    public CinemasTypeResponse handleAddNewCinemasType(CinemasTypeNewOrUpdateRequest cinemasTypeNewRequest) {
        CinemasType cinemasType = cinemasTypeMapper.toCinemasTypeNewOrUpdateRequestToCinemasType(cinemasTypeNewRequest);
        cinemasType.setAvatar(cinemasTypeNewRequest.getSrcAvatar());
        cinemasType.setStatus(true);
        cinemasType.setCreateAt(new Date());
        cinemasType.setUpdateAt(new Date());
        cinemasTypeRepository.save(cinemasType);
        return cinemasTypeMapper.toCinemasTypeToCinemasTypeResponse(cinemasType);
    }

    public  DataListResponseWithPagination<List<CinemasTypeResponse>> handleGetAllCinemasType(PaginationRequestParams paginationParams, GetAllCinemasTypeFilterParams filterParams) {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<CinemasType> cinemasTypeListMatchedFilterLimited = new ArrayList<>();
        List<CinemasType> cinemasTypeListMatchedFilter = new ArrayList<>();
        List<CinemasType> cinemasTypeListInDB = new ArrayList<>();

        if (Objects.isNull(filterParams.getSearchValue())) {
            cinemasTypeListInDB = cinemasTypeRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            cinemasTypeListInDB = cinemasTypeRepository.findAllByNameContainingIgnoreCase(searchValue);
        }
        List<String> filterByFields = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFields.add("status");


        for (CinemasType cinemasType : cinemasTypeListInDB) {
            boolean checkMatch = isCheckCinemasTypeMatchFilter(filterParams, cinemasType, filterByFields);
            if (checkMatch) {
                cinemasTypeListMatchedFilter.add(cinemasType);
            }
        }
        int totalElements = cinemasTypeListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());

        // Cinemas type list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                cinemasTypeListMatchedFilterLimited.add(cinemasTypeListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            cinemasTypeListMatchedFilterLimited = cinemasTypeListMatchedFilter;
        }

        List<CinemasTypeResponse> cinemasTypeRespostList = cinemasTypeListMatchedFilterLimited.stream().map(cinemasTypeMapper::toCinemasTypeToCinemasTypeResponse).toList();
        return DataListResponseWithPagination.<List<CinemasTypeResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(cinemasTypeRespostList)
                .build();
    }

    public CinemasTypeResponse handleUpdateCinemasType(String id, CinemasTypeNewOrUpdateRequest cinemasTypeUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.CINEMASTYPE_NOT_EXISTED;
        CinemasType cinemasType = cinemasTypeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(), errorCode.getStatusCode().value()));
        cinemasTypeMapper.updateCinemasType(cinemasType, cinemasTypeUpdateRequest);
        if (!Objects.equals(cinemasTypeUpdateRequest.getSrcAvatar(), cinemasType.getAvatar())) {
            String[] arrayStringAvatarSplit = cinemasType.getAvatar().split("/");
            int indexGetFileNameAvatar = arrayStringAvatarSplit.length - 1;
            String fileNameAvatar = arrayStringAvatarSplit[indexGetFileNameAvatar];
            try {
                fileService.deleteFileStoraged(fileNameAvatar);
            } catch (Exception ex) {
                log.error("Error when delete avatar of cinemas type{}", ex.getMessage());
            }
            cinemasType.setAvatar(cinemasTypeUpdateRequest.getSrcAvatar());
        }
        cinemasType.setUpdateAt(new Date());
        cinemasTypeRepository.save(cinemasType);
        return cinemasTypeMapper.toCinemasTypeToCinemasTypeResponse(cinemasType);
    }

    private static boolean isCheckCinemasTypeMatchFilter(GetAllCinemasTypeFilterParams filterParams, CinemasType cinemasType, List<String> filterByFields) {
        boolean checkMatch = true;
        if (filterByFields.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == cinemasType.isStatus();
        }
        return checkMatch;
    }


//    public boolean handleDeleteCinemasType(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.POPCOM_NOT_EXISTED;
//        CinemasType cinemasType = cinemasTypeRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        cinemasTypeRepository.deleteById(cinemasType.getId());
//        String[] arrayStringAvatarSplit = cinemasType.getAvatar().split("/");
//        int indexGetFileNameAvatar = arrayStringAvatarSplit.length - 1;
//        String fileNameAvatar = arrayStringAvatarSplit[indexGetFileNameAvatar];
//        try {
//            fileService.deleteFileStoraged(fileNameAvatar);
//        } catch (Exception ex) {
//            log.error("Error when delete avatar of cinemas type{}", ex.getMessage());
//        }
//        return true;
//    }
}
