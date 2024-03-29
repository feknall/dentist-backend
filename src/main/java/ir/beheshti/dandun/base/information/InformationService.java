package ir.beheshti.dandun.base.information;

import ir.beheshti.dandun.base.notification.NotificationEntity;
import ir.beheshti.dandun.base.notification.NotificationInputDto;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
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

    public void addInformation(InformationInputDto informationInputDto) {
        informationRepository.save(informationInputDto.toEntity());
    }

    public void updateInformation(InformationInputDto informationInputDto) {
        Optional<InformationEntity> entity = informationRepository.findById(informationInputDto.getInformationId());
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.INFORMATION_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.INFORMATION_NOT_FOUND_MESSAGE);
        }
        InformationEntity informationEntity = informationInputDto.toEntity();
        informationEntity.setInformationId(informationInputDto.getInformationId());
        informationRepository.save(informationEntity);
    }

    public void deleteInformation(int informationId) {
        Optional<InformationEntity> entity = informationRepository.findById(informationId);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.INFORMATION_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.INFORMATION_NOT_FOUND_MESSAGE);
        }
        informationRepository.deleteById(informationId);
    }
}
