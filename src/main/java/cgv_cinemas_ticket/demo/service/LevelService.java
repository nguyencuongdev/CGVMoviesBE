package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.PaginationRequestParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllLevelFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.GetAllPopcomFilterParams;
import cgv_cinemas_ticket.demo.dto.request.admin.LevelNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.DataListResponseWithPagination;
import cgv_cinemas_ticket.demo.dto.response.admin.LevelResponse;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ILevelMapper;
import cgv_cinemas_ticket.demo.model.Level;
import cgv_cinemas_ticket.demo.model.Popcom;
import cgv_cinemas_ticket.demo.repository.ILevelRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LevelService {
    ILevelRepository levelRepository;
    ILevelMapper levelMapper;

    public LevelResponse handleAddNewLevel(LevelNewOrUpdateRequest levelNewRequest) {
        Level level = levelMapper.toLevelNewOrUpdateRequestToLevel(levelNewRequest);
        level.setCreateAt(new Date());
        level.setUpdateAt(new Date());
        levelRepository.save(level);
        return levelMapper.toLevelToLevelResponse(level);
    }

    public DataListResponseWithPagination<List<LevelResponse>> handleGetAllLevel (PaginationRequestParams paginationParams, GetAllLevelFilterParams filterParams) {
        int page = paginationParams.getPage();
        int size = paginationParams.getSize();
        List<Level> levelListMatchedFilterLimited = new ArrayList<>();
        List<Level> levelListMatchedFilter = new ArrayList<>();
        List<Level> levelListInDB = new ArrayList<>();
        if (Objects.isNull(filterParams.getSearchValue())) {
            levelListInDB = levelRepository.findAll();
        } else {
            String searchValue = filterParams.getSearchValue();
            levelListInDB = levelRepository.findAllByNameContainingIgnoreCase(searchValue);
        }
        List<String> filterByFileds = new ArrayList<>();
        if (Objects.nonNull(filterParams.getStatus())) filterByFileds.add("status");

        for (Level level : levelListInDB) {
            boolean checkMatch = isCheckLevelMatchFilter(filterParams, level, filterByFileds);
            if (checkMatch) {
                levelListMatchedFilter.add(level);
            }
        }
        int totalElements = levelListMatchedFilter.size();
        int totalPages = (int) Math.ceil((double) totalElements / paginationParams.getSize());
//           Level list matched filter limited
        if (totalPages > 1) {
            for (int index = page * size; index < (size * page + size) && index < totalElements; index++) {
                levelListMatchedFilterLimited.add(levelListMatchedFilter.get(index));
            }
        } else if (totalPages == 1 && page == 0) {
            levelListMatchedFilterLimited = levelListMatchedFilter;
        }
        List<LevelResponse> cinemasResponseList = levelListMatchedFilterLimited.stream().map(levelMapper::toLevelToLevelResponse).toList();
        return DataListResponseWithPagination.<List<LevelResponse>>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(cinemasResponseList)
                .build();
    }

    public LevelResponse handleUpdateLevel(String id, LevelNewOrUpdateRequest leveUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.LEVEL_NOT_EXISTED;
        Level level = levelRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));

        levelMapper.updateLevel(level,leveUpdateRequest);
        level.setUpdateAt(new Date());
        levelRepository.save(level);
        return levelMapper.toLevelToLevelResponse(level);
    }

    private static boolean isCheckLevelMatchFilter(GetAllLevelFilterParams filterParams, Level level, List<String> filterByFileds) {
        boolean checkMatch = true;
        if (filterByFileds.contains("status")) {
            boolean status = Objects.equals(filterParams.getStatus(), "1");
            checkMatch = status == level.isStatus();
        }
        return checkMatch;
    }
//    public boolean handleDeleteLevel(String id) throws AppException {
//        ErrorCode errorCode = ErrorCode.LEVEL_NOT_EXISTED;
//        Level level = levelRepository.findById(Long.parseLong(id))
//                .orElseThrow(() ->
//                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
//        levelRepository.deleteById(level.getId());
//        return true;
//    }
}
