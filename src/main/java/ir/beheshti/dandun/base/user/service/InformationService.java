package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.information.InformationOutputDto;
import ir.beheshti.dandun.base.user.entity.InformationEntity;
import ir.beheshti.dandun.base.user.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InformationService {

    @Autowired
    private InformationRepository informationRepository;

    public List<InformationOutputDto> getAllInformation() {
        return informationRepository
                .findAll()
                .stream()
                .map(InformationOutputDto::fromEntityMinimum)
                .collect(Collectors.toList());
    }

    public InformationOutputDto getInformationById(int id) {
        Optional<InformationEntity> entity = informationRepository.findById(id);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.INFORMATION_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.INFORMATION_NOT_FOUND_MESSAGE);
        }
        return InformationOutputDto.fromEntity(entity.get());
    }

}
