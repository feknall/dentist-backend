package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.question.*;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    private final UserRepository userRepository;
    private final EssentialQuestionRepository essentialQuestionRepository;
    private final GeneralService generalService;
    private final UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository;
    private final UserTrueFalseQuestionAnswerRepository userTrueFalseQuestionAnswerRepository;
    private final UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    private final OperatorService operatorService;

    public EssentialQuestionService(UserRepository userRepository,
                                    EssentialQuestionRepository essentialQuestionRepository,
                                    GeneralService generalService,
                                    UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository,
                                    UserTrueFalseQuestionAnswerRepository userTrueFalseQuestionAnswerRepository,
                                    UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository,
                                    MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository, OperatorService operatorService) {
        this.userRepository = userRepository;
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.generalService = generalService;
        this.userOpenQuestionAnswerRepository = userOpenQuestionAnswerRepository;
        this.userTrueFalseQuestionAnswerRepository = userTrueFalseQuestionAnswerRepository;
        this.userMultipleQuestionAnswerRepository = userMultipleQuestionAnswerRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
        this.operatorService = operatorService;
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

    public List<UserQuestionAnswerOutputDto> getUserAnswersByUser() {
        return getUserAnswers(generalService.getCurrentUserId());
    }

    public List<UserQuestionAnswerOutputDto> getUserAnswersByOperator(int userId) {
        return getUserAnswers(userId);
    }

    private List<UserQuestionAnswerOutputDto> getUserAnswers(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        List<UserQuestionAnswerOutputDto> outputDtoList = new ArrayList<>();
        userEntity
                .get()
                .getUserOpenQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofOpenAnswer).forEach(outputDtoList::add);
        userEntity
                .get()
                .getUserTrueFalseQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofTrueFalse).forEach(outputDtoList::add);
        if (!userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().isEmpty()) {
            outputDtoList.add(UserQuestionAnswerOutputDto.ofMultipleChoice(userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList()));
        }
        return outputDtoList;
    }

    public IsCompleteAnswerOutputDto isUserAnswersComplete() {
        IsCompleteAnswerOutputDto dto = new IsCompleteAnswerOutputDto();
        dto.setComplete(!getUserAnswersByUser().isEmpty());
        dto.setPatientStateType(operatorService.getPatientStateByUser().getPatientStateType());
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
