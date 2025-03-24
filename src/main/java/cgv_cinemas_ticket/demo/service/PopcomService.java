package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.admin.PopcomNewOrUpdateRequest;
import cgv_cinemas_ticket.demo.dto.response.admin.PopcomResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.IPopcomMapper;
import cgv_cinemas_ticket.demo.model.Popcom;
import cgv_cinemas_ticket.demo.repository.IPopcomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<PopcomResponse> handleGetAllPopcom() {
        List<Popcom> popcoms = popcomRepository.findAll();
        return popcoms.stream().map(popcomMapper::toPopcomToPopcomResponse).toList();
    }

    public PopcomResponse handleUpdatePopcom(String id, PopcomNewOrUpdateRequest popcomUpdateRequest) throws AppException {
        ErrorCode errorCode = ErrorCode.POPCOM_NOT_EXISTED;
        Popcom popcom = popcomRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));

        popcomMapper.updatePopcom(popcom,popcomUpdateRequest);
        popcomRepository.save(popcom);
        return popcomMapper.toPopcomToPopcomResponse(popcom);
    }
    public boolean handleDeletePopcom(String id) throws AppException {
        ErrorCode errorCode = ErrorCode.POPCOM_NOT_EXISTED;
        Popcom popcom = popcomRepository.findById(Long.parseLong(id))
                .orElseThrow(() ->
                        new AppException(errorCode.getMessage(),errorCode.getStatusCode().value()));
        popcomRepository.deleteById(popcom.getId());
        return true;
    }
}
