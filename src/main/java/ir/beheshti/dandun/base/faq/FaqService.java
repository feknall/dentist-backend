package ir.beheshti.dandun.base.faq;

import ir.beheshti.dandun.base.information.InformationEntity;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    public List<FaqOutputDto> getAllFaq() {
        return faqRepository
                .findAll()
                .stream()
                .map(FaqOutputDto::fromEntityMinimum)
                .collect(Collectors.toList());
    }

    public FaqOutputDto getFaqById(int id) {
        Optional<FaqEntity> entity = faqRepository.findById(id);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.FAQ_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.FAQ_NOT_FOUND_MESSAGE);
        }
        return FaqOutputDto.fromEntityFull(entity.get());
    }

    public void addFaq(FaqInputDto faqInputDto) {
        faqRepository.save(faqInputDto.toEntity());
    }

    public void updateFaq(FaqInputDto faqInputDto) {
        Optional<FaqEntity> entity = faqRepository.findById(faqInputDto.getFaqId());
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.FAQ_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.FAQ_NOT_FOUND_MESSAGE);
        }
        FaqEntity faqEntity = faqInputDto.toEntity();
        faqEntity.setFaqId(faqInputDto.getFaqId());
        faqRepository.save(faqEntity);
    }

    public void deleteFaq(int faqId) {
        Optional<FaqEntity> entity = faqRepository.findById(faqId);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.FAQ_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.FAQ_NOT_FOUND_MESSAGE);
        }
        faqRepository.deleteById(faqId);
    }
}
