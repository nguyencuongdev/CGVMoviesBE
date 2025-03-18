package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.LevelNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.LevelResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ILevelMapper;
import cgv_cinemas_ticket.demo.model.Level;
import cgv_cinemas_ticket.demo.repository.ILevelRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List<LevelResponse> handleGetAllLevel() {
        List<Level> levels = levelRepository.findAll();
        return levels.stream().map(levelMapper::toLevelToLevelResponse).toList();
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
    public boolean handleDeleteLevel(String id) throws AppException {
        ErrorCode errorCode = ErrorCode.LEVEL_NOT_EXISTED;
        Level level = levelRepository.findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
        levelRepository.deleteById(level.getId());
        return true;
    }
}
