package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.admin.CinemasTypeNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.CinemasTypeResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.ICinemasTypeMapper;
import cgv_cinemas_ticket.demo.model.CinemasType;
import cgv_cinemas_ticket.demo.repository.ICinemasTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public List<CinemasTypeResponse> handleGetAllCinemasType() {
        List<CinemasType> cinemasTypeList = cinemasTypeRepository.findAll();
        return cinemasTypeList.stream().map(cinemasTypeMapper::toCinemasTypeToCinemasTypeResponse).toList();
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
