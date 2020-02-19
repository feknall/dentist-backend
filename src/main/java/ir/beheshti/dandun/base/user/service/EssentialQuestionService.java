package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.repository.EssentialQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    private final EssentialQuestionRepository essentialQuestionRepository;

    public EssentialQuestionService(EssentialQuestionRepository essentialQuestionRepository) {
        this.essentialQuestionRepository = essentialQuestionRepository;
    }

    public List<QuestionOutputDto> getAll() {
        List<EssentialQuestionEntity> questions = essentialQuestionRepository.findAll();
        return questions
                .stream()
                .map(QuestionOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public QuestionOutputDto getById(int questionId) {
        Optional<EssentialQuestionEntity> question = essentialQuestionRepository.findById(questionId);
        if (question.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.QUESTION_NOT_FOUND_CODE, ErrorCodeAndMessage.StringQUESTION_NOT_FOUND_MESSAGE);
        }
        return QuestionOutputDto.fromEntity(question.get());
    }


}
