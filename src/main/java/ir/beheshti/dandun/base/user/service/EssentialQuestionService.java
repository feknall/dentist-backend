package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.question.*;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
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

    public EssentialQuestionService(PatientRepository patientRepository, DoctorRepository doctorRepository, UserRepository userRepository,
                                    EssentialQuestionRepository essentialQuestionRepository,
                                    GeneralService generalService,
                                    UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository,
                                    UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository,
                                    UserRangeQuestionAnswerRepository userRangeQuestionAnswerRepository, UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository,
                                    MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository, OperatorService operatorService,
                                    UserImageQuestionAnswerRepository userImageQuestionAnswerRepository, UtilityService utilityService) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
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
        Optional<QuestionOwnerType> ownerType = generalService.getQuestionOwnerTypeFromCurrentUser();
        if (ownerType.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.QUESTION_OWNER_NOT_ALLOWED_CODE, ErrorCodeAndMessage.QUESTION_OWNER_NOT_ALLOWED_MESSAGE);
        }
        return getAllQuestionsByQuestionOwner(ownerType.get());
    }

    private List<QuestionOutputDto> getAllQuestionsByQuestionOwner(QuestionOwnerType ownerType) {
        List<EssentialQuestionEntity> questions = essentialQuestionRepository.findAllByQuestionOwnerTypeEqualsOrQuestionOwnerTypeEquals(ownerType, QuestionOwnerType.Public);
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
        inputDto.getImageList().forEach(image -> {
            UserImageQuestionAnswerEntity userImageQuestionAnswerEntity = new UserImageQuestionAnswerEntity();
            userImageQuestionAnswerEntity.setUserId(currentUserId);
            userImageQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
            userImageQuestionAnswerEntity.setImage(utilityService.toByteWrapper(image.getBytes()));
            entityList.add(userImageQuestionAnswerEntity);
        });

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
        } else if (questionType != null && !questionEntityOptional.get().getQuestionType().equals(questionType)) {
            throw new UserException(ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_CODE, ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_MESSAGE);
        } else {
            Optional<QuestionOwnerType> ownerType = generalService.getQuestionOwnerTypeFromCurrentUser();
            if (ownerType.isEmpty()) {
                throw new UserException(ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_CODE, ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_MESSAGE);
            }
            if (!ownerType.get().equals(questionEntityOptional.get().getQuestionOwnerType())) {
                throw new UserException(ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_CODE, ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_MESSAGE);
            }
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
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(generalService.getCurrentUserId());
        if (patientUserEntityOptional.isPresent()) {
            dto.setComplete(!patientUserEntityOptional.get().getPatientStateType().equals(PatientStateType.NOT_ANSWERED));
            dto.setPatientStateType(patientUserEntityOptional.get().getPatientStateType());
            return dto;
        }

        Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(generalService.getCurrentUserId());
        if (doctorUserEntityOptional.isPresent()) {
            dto.setComplete(!doctorUserEntityOptional.get().getDoctorStateType().equals(DoctorStateType.NOT_ANSWERED));
            dto.setDoctorStateType(doctorUserEntityOptional.get().getDoctorStateType());
            return dto;
        }

        throw new UserException(ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_CODE, ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_MESSAGE);
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

        updateUserStateAfterAnsweringQuestions(currentUserId);
    }

    private void updateUserStateAfterAnsweringQuestions(int currentUserId) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        if (userEntity.getUserType().equals(UserType.Patient)) {
            Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(currentUserId);
            if (patientUserEntityOptional.isEmpty()) {
                throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                        ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
            }
            patientUserEntityOptional.get().setPatientStateType(PatientStateType.PENDING);
            patientRepository.save(patientUserEntityOptional.get());
        } else if (userEntity.getUserType().equals(UserType.Doctor)) {
            Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(currentUserId);
            if (doctorUserEntityOptional.isEmpty()) {
                throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                        ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
            }
            doctorUserEntityOptional.get().setDoctorStateType(DoctorStateType.PENDING);
            doctorRepository.save(doctorUserEntityOptional.get());
        }
    }

    public List<ImageAnswerOutputDto> getUserImageAnswer(ImageAnswerInputDto imageAnswerInputDto) {
        int currentUserId = generalService.getCurrentUserId();
        List<UserImageQuestionAnswerEntity> imageList = userImageQuestionAnswerRepository.findAllByUserIdAndEssentialQuestionId(currentUserId, imageAnswerInputDto.getQuestionId());
        List<ImageAnswerOutputDto> outputDtoList = new ArrayList<>();
        imageList.forEach(image -> {
            ImageAnswerOutputDto outputDto = new ImageAnswerOutputDto();
            outputDto.setImage(Arrays.toString(image.getImage()));
            outputDto.setImageId(image.getId());
            outputDto.setQuestionId(image.getEssentialQuestionId());
            outputDtoList.add(outputDto);
        });
        return outputDtoList;
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

    @Transactional
    public void deleteAllAnswers() {
        int currentUserId = generalService.getCurrentUserId();
        userImageQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userMultipleQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userRangeQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userOpenQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userSingleQuestionAnswerRepository.deleteAllByUserId(currentUserId);
    }

    public void deleteImageById(Integer imageId) {
        int currentUserId = generalService.getCurrentUserId();
        Optional<UserImageQuestionAnswerEntity> imageQuestionAnswerEntityOptional = userImageQuestionAnswerRepository.findByIdAndUserId(imageId, currentUserId);
        if (imageQuestionAnswerEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.ANSWER_IMAGE_NOT_FOUND_CODE, ErrorCodeAndMessage.ANSWER_IMAGE_NOT_FOUND_MESSAGE);
        }
        userImageQuestionAnswerRepository.delete(imageQuestionAnswerEntityOptional.get());
    }

    public void deleteAllImageAnswers() {
        userImageQuestionAnswerRepository.deleteAllByUserId(generalService.getCurrentUserId());
    }
}
