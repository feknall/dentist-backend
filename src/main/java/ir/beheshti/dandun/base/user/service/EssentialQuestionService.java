package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.question.*;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    private final EssentialQuestionRepository essentialQuestionRepository;
    private final GeneralService generalService;
    private final UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository;
    private final UserTrueFalseQuestionAnswerRepository userTrueFalseQuestionAnswerRepository;
    private final UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;

    public EssentialQuestionService(EssentialQuestionRepository essentialQuestionRepository, GeneralService generalService, UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository, UserTrueFalseQuestionAnswerRepository userTrueFalseQuestionAnswerRepository, UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository, MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository) {
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.generalService = generalService;
        this.userOpenQuestionAnswerRepository = userOpenQuestionAnswerRepository;
        this.userTrueFalseQuestionAnswerRepository = userTrueFalseQuestionAnswerRepository;
        this.userMultipleQuestionAnswerRepository = userMultipleQuestionAnswerRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
    }

    public List<QuestionOutputDto> getAll() {
        List<EssentialQuestionEntity> questions = essentialQuestionRepository.findAll();
        return questions
                .stream()
                .map(QuestionOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public QuestionOutputDto getById(int questionId) {
        return QuestionOutputDto.fromEntity(validateQuestionExistence(questionId));
    }

    public void fillOpenAnswer(OpenAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.Open);
        saveOrUpdateOpenAnswer(inputDto.getQuestionId(), inputDto.getDescription());
    }

    @Transactional
    public void fillMultipleChoiceAnswer(MultipleChoiceAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.MultipleChoice);

        int currentUserId = generalService.getCurrentUserId();
        if (userMultipleQuestionAnswerRepository
                .existsByUserIdAndMultipleChoiceQuestionAnswerIdIsIn(currentUserId, inputDto.getAnswerIdList())) {
            throw new UserException(ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE);
        }

        if (multipleChoiceQuestionAnswerRepository.countByIdInAndEssentialQuestionEntityId(inputDto.getAnswerIdList(), inputDto.getQuestionId())
                != inputDto.getAnswerIdList().size()) {
            throw new UserException(ErrorCodeAndMessage.SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_MESSAGE);
        }

        List<UserMultipleChoiceQuestionAnswerEntity> userAnswerEntityList = new ArrayList<>();
        for (int i: inputDto.getAnswerIdList()) {
            UserMultipleChoiceQuestionAnswerEntity userAnswerEntity = new UserMultipleChoiceQuestionAnswerEntity();
            userAnswerEntity.setUserId(currentUserId);
            userAnswerEntity.setMultipleChoiceQuestionAnswerId(i);
            userAnswerEntityList.add(userAnswerEntity);
        }
        userMultipleQuestionAnswerRepository.saveAll(userAnswerEntityList);
    }

    @Transactional
    public void fillTrueFalseAnswer(TrueFalseAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.TrueFalse);

        int currentUserId = generalService.getCurrentUserId();
        if (userTrueFalseQuestionAnswerRepository.existsByUserIdAndEssentialQuestionId(currentUserId, inputDto.getQuestionId())) {
            throw new UserException(ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE);
        }

        UserTrueFalseQuestionAnswerEntity userTrueFalseQuestionAnswerEntity = new UserTrueFalseQuestionAnswerEntity();
        userTrueFalseQuestionAnswerEntity.setUserId(currentUserId);
        userTrueFalseQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
        userTrueFalseQuestionAnswerEntity.setAnswer(inputDto.getAnswer());
        userTrueFalseQuestionAnswerRepository.save(userTrueFalseQuestionAnswerEntity);
    }


    private void saveOrUpdateOpenAnswer(int questionId, String description) {
        int currentUserId = generalService.getCurrentUserId();
        UserOpenQuestionAnswerEntity userOpenQuestionAnswerEntity;
        Optional<UserOpenQuestionAnswerEntity> userOpenQuestionAnswerEntityOptional = userOpenQuestionAnswerRepository.findByUserIdAndEssentialQuestionId(currentUserId, questionId);
        if (userOpenQuestionAnswerEntityOptional.isEmpty()) {
            userOpenQuestionAnswerEntity = new UserOpenQuestionAnswerEntity();
            userOpenQuestionAnswerEntity.setUserId(currentUserId);
            userOpenQuestionAnswerEntity.setEssentialQuestionId(questionId);
        } else {
            userOpenQuestionAnswerEntity = userOpenQuestionAnswerEntityOptional.get();
        }
        userOpenQuestionAnswerEntity.setDescription(description);
        userOpenQuestionAnswerRepository.save(userOpenQuestionAnswerEntity);
    }

    private EssentialQuestionEntity validateQuestionExistence(int questionId) {
        return validateQuestionExistence(questionId, null);
    }

    private EssentialQuestionEntity validateQuestionExistence(int questionId, QuestionType questionType) {
        Optional<EssentialQuestionEntity> questionEntityOptional = essentialQuestionRepository.findById(questionId);
        if (questionEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.QUESTION_NOT_FOUND_CODE, ErrorCodeAndMessage.QUESTION_NOT_FOUND_MESSAGE);
        }
        if (questionType != null && !questionEntityOptional.get().getQuestionType().equals(questionType)) {
           throw new UserException(ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_CODE, ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_MESSAGE);
        }
        return questionEntityOptional.get();
    }

    public List<UserQuestionAnswerOutputDto> getUserAnswers() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        List<UserQuestionAnswerOutputDto> outputDtoList = new ArrayList<>();
        userEntity
                .getUserOpenQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofOpenAnswer).forEach(outputDtoList::add);
        userEntity
                .getUserTrueFalseQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofTrueFalse).forEach(outputDtoList::add);
        if (!userEntity.getUserMultipleChoiceQuestionAnswerEntityList().isEmpty()) {
            outputDtoList.add(UserQuestionAnswerOutputDto.ofMultipleChoice(userEntity.getUserMultipleChoiceQuestionAnswerEntityList()));
        }
        return outputDtoList;
    }

    public IsCompleteAnswerOutputDto isUserAnswersComplete() {
        IsCompleteAnswerOutputDto dto = new IsCompleteAnswerOutputDto();
        dto.setComplete(getUserAnswers().size() == 6);
        return dto;
    }

    @Transactional
    public void fillAllAnswers(AllAnswerOpenDto allAnswerOpenDto) {
        int currentUserId = generalService.getCurrentUserId();
        userMultipleQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getMultipleChoiceAnswerInputDtoList().forEach(this::fillMultipleChoiceAnswer);

        userOpenQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getOpenAnswerInputDtoList().forEach(this::fillOpenAnswer);

        userTrueFalseQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getTrueFalseAnswerInputDtoList().forEach(this::fillTrueFalseAnswer);
    }
}
