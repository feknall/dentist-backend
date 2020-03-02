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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    private final UserRepository userRepository;
    private final EssentialQuestionRepository essentialQuestionRepository;
    private final GeneralService generalService;
    private final UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository;
    private final UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository;
    private final UserRangeQuestionAnswerRepository userRangeQuestionAnswerRepository;
    private final UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;
    private final MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    private final OperatorService operatorService;
    private final UserImageQuestionAnswerRepository userImageQuestionAnswerRepository;
    private final UtilityService utilityService;

    public EssentialQuestionService(UserRepository userRepository,
                                    EssentialQuestionRepository essentialQuestionRepository,
                                    GeneralService generalService,
                                    UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository,
                                    UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository,
                                    UserRangeQuestionAnswerRepository userRangeQuestionAnswerRepository, UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository,
                                    MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository, OperatorService operatorService, UserImageQuestionAnswerRepository userImageQuestionAnswerRepository, UtilityService utilityService) {
        this.userRepository = userRepository;
        this.essentialQuestionRepository = essentialQuestionRepository;
        this.generalService = generalService;
        this.userOpenQuestionAnswerRepository = userOpenQuestionAnswerRepository;
        this.userSingleQuestionAnswerRepository = userSingleQuestionAnswerRepository;
        this.userRangeQuestionAnswerRepository = userRangeQuestionAnswerRepository;
        this.userMultipleQuestionAnswerRepository = userMultipleQuestionAnswerRepository;
        this.multipleChoiceQuestionAnswerRepository = multipleChoiceQuestionAnswerRepository;
        this.operatorService = operatorService;
        this.userImageQuestionAnswerRepository = userImageQuestionAnswerRepository;
        this.utilityService = utilityService;
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
        for (int i : inputDto.getAnswerIdList()) {
            UserMultipleChoiceQuestionAnswerEntity userAnswerEntity = new UserMultipleChoiceQuestionAnswerEntity();
            userAnswerEntity.setUserId(currentUserId);
            userAnswerEntity.setMultipleChoiceQuestionAnswerId(i);
            userAnswerEntityList.add(userAnswerEntity);
        }
        userMultipleQuestionAnswerRepository.saveAll(userAnswerEntityList);
    }

    @Transactional
    public void fillSingleAnswer(SingleAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.SingleChoice);

        int currentUserId = generalService.getCurrentUserId();
        if (userSingleQuestionAnswerRepository.existsByUserIdAndEssentialQuestionId(currentUserId, inputDto.getQuestionId())) {
            throw new UserException(ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE);
        }

        UserSingleQuestionAnswerEntity userSingleQuestionAnswerEntity = new UserSingleQuestionAnswerEntity();
        userSingleQuestionAnswerEntity.setUserId(currentUserId);
        userSingleQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
        userSingleQuestionAnswerEntity.setMultipleChoiceQuestionAnswerId(inputDto.getAnswerId());
        userSingleQuestionAnswerRepository.save(userSingleQuestionAnswerEntity);
    }

    @Transactional
    public void fillRangeAnswer(RangeAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.Range);

        int currentUserId = generalService.getCurrentUserId();
        if (userSingleQuestionAnswerRepository.existsByUserIdAndEssentialQuestionId(currentUserId, inputDto.getQuestionId())) {
            throw new UserException(ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE);
        }

        UserRangeQuestionAnswerEntity userRangeQuestionAnswerEntity = new UserRangeQuestionAnswerEntity();
        userRangeQuestionAnswerEntity.setUserId(currentUserId);
        userRangeQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
        userRangeQuestionAnswerEntity.setValue(inputDto.getValue());
        userRangeQuestionAnswerRepository.save(userRangeQuestionAnswerEntity);
    }

    @Transactional
    public void fillImageAnswer(ImageAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.Image);

        int currentUserId = generalService.getCurrentUserId();

        List<UserImageQuestionAnswerEntity> entityList = new ArrayList<>();
        UserImageQuestionAnswerEntity userImageQuestionAnswerEntity = new UserImageQuestionAnswerEntity();
        userImageQuestionAnswerEntity.setUserId(currentUserId);
        userImageQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
        userImageQuestionAnswerEntity.setImage(utilityService.toByteWrapper(inputDto.getImage().getBytes()));
        entityList.add(userImageQuestionAnswerEntity);

        userImageQuestionAnswerRepository.saveAll(entityList);
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

        // Open Answers
        userEntity
                .get()
                .getUserOpenQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofOpenAnswer).forEach(outputDtoList::add);

        // Single-Choice Answers
        userEntity
                .get()
                .getUserSingleQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofSingle).forEach(outputDtoList::add);

        // Range Answers
        userEntity
                .get()
                .getUserRangeQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofRange).forEach(outputDtoList::add);

        // Multiple-Choice Answers
        if (!userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().isEmpty()) {
            outputDtoList.add(UserQuestionAnswerOutputDto
                    .ofMultipleChoice(userEntity
                            .get()
                            .getUserMultipleChoiceQuestionAnswerEntityList()));
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

        userRangeQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getRangeAnswerInputDtoList().forEach(this::fillRangeAnswer);

        userOpenQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getOpenAnswerInputDtoList().forEach(this::fillOpenAnswer);

        userSingleQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getSingleAnswerInputDtoList().forEach(this::fillSingleAnswer);
    }

    public ImageIdsOutputDto getUserImageAnswerIds(ImageAnswerInputDto imageAnswerInputDto) {
        int currentUserId = generalService.getCurrentUserId();
        List<Integer> imageIds = userImageQuestionAnswerRepository.findAllByUserIdAndEssentialQuestionId(currentUserId, imageAnswerInputDto.getQuestionId());
        ImageIdsOutputDto outputDto = new ImageIdsOutputDto();
        outputDto.setImageIds(imageIds);
        return outputDto;
    }

    public ImageAnswerOutputDto getUserImageAnswer(Integer imageId) {
        int currentUserId = generalService.getCurrentUserId();
        Optional<UserImageQuestionAnswerEntity> entity = userImageQuestionAnswerRepository.findByIdAndUserId(imageId, currentUserId);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.ANSWER_IMAGE_NOT_FOUND_CODE, ErrorCodeAndMessage.ANSWER_IMAGE_NOT_FOUND_MESSAGE);
        }
        ImageAnswerOutputDto outputDto = new ImageAnswerOutputDto();
        outputDto.setImage(Arrays.toString(entity.get().getImage()));
        return outputDto;
    }
}
