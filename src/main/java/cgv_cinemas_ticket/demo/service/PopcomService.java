package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllCinemasFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllPopcomFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.IPopcomMapper;
import cgv_cinemas_ticket.demo.model.Cinemas;
import cgv_cinemas_ticket.demo.model.Popcom;
import cgv_cinemas_ticket.demo.repository.IPopcomRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PopcomService {
    IPopcomRepository popcomRepository;
    IPopcomMapper popcomMapper;

    public PopcomResponse handleAddNewPopcom(PopcomNewOrUpdateRequest popcomNewRequest) {
        Popcom popcom = popcomMapper.toPopcomNewOrUpdateRequestToPopcom(popcomNewRequest);
        popcomRepository.save(popcom);
        return popcomMapper.toPopcomToPopcomResponse(popcom);
    }

    public  DataListResponseWithPagination<List<PopcomResponse>> handleGetAllPopcom( PaginationRequestParams paginationParams,  GetAllPopcomFilterParams filterParams) {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<Popcom> popcomListMatchedFilterLimited = new ArrayList<>();
        List<Popcom> popcomListMatchedFilter = new ArrayList<>();
        List<Popcom> popcomListInDB = new ArrayList<>();
        if (Objects.isNull(filterParams.getSearchValue())) {
            popcomListInDB = popcomRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            popcomListInDB = popcomRepository.findAllByNameContainingIgnoreCase(searchValue);
        }
        List<String> filterByFileds = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFileds.add("status");


        for (Popcom popcom : popcomListInDB) {
            boolean checkMatch = isCheckPopcomMatchFilter(filterParams, popcom, filterByFileds);
            if (checkMatch) {
                popcomListMatchedFilter.add(popcom);
            }
        }
        int totalElements = popcomListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());
//           Popcom list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                popcomListMatchedFilterLimited.add(popcomListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            popcomListMatchedFilterLimited = popcomListMatchedFilter;
        }
        List<PopcomResponse> cinemasResponseList = popcomListMatchedFilterLimited.stream().map(popcomMapper::toPopcomToPopcomResponse).toList();
        return DataListResponseWithPagination.<List<PopcomResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(cinemasResponseList)
                .build();
    }

    public PopcomResponse handleUpdatePopcom(String id, PopcomNewOrUpdateRequest popcomUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.POPCOM_NOT_EXISTED;
        Popcom popcom = popcomRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));

        popcomMapper.updatePopcom(popcom,popcomUpdateRequest);
        popcomRepository.save(popcom);
        return popcomMapper.toPopcomToPopcomResponse(popcom);
    }

    private static boolean isCheckPopcomMatchFilter(GetAllPopcomFilterParams filterParams, Popcom cinemas, List<String> filterByFileds) {
        boolean checkMatch = true;
        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == cinemas.isStatus();
        }
        return checkMatch;
    }
//    public boolean handleDeletePopcom(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.POPCOM_NOT_EXISTED;
//        Popcom popcom = popcomRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        popcomRepository.deleteById(popcom.getId());
//        return true;
//    }
}
