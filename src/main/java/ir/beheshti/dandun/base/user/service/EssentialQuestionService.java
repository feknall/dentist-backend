package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.question.*;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialQuestionService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private EssentialQuestionRepository essentialQuestionRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserOpenQuestionAnswerRepository userOpenQuestionAnswerRepository;
    @Autowired
    private UserSingleQuestionAnswerRepository userSingleQuestionAnswerRepository;
    @Autowired
    private UserRangeQuestionAnswerRepository userRangeQuestionAnswerRepository;
    @Autowired
    private UserMultipleQuestionAnswerRepository userMultipleQuestionAnswerRepository;
    @Autowired
    private MultipleChoiceQuestionAnswerRepository multipleChoiceQuestionAnswerRepository;
    @Autowired
    private UserImageQuestionAnswerRepository userImageQuestionAnswerRepository;
    @Autowired
    private UserOpenNumberQuestionAnswerRepository userOpenNumberQuestionAnswerRepository;
    @Autowired
    private OperatorService operatorService;

    public List<QuestionOutputDto> getAll() {
        Optional<QuestionOwnerType> ownerType = generalService.getQuestionOwnerTypeFromCurrentUser();
        if (ownerType.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.QUESTION_OWNER_NOT_ALLOWED_CODE, ErrorCodeAndMessage.QUESTION_OWNER_NOT_ALLOWED_MESSAGE);
        }
        return getAllQuestionsByQuestionOwner(ownerType.get());
    }

    public List<QuestionOutputDto> getAllQuestionsByQuestionOwner(QuestionOwnerType ownerType) {
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

        if (!multipleChoiceQuestionAnswerRepository.existsById(inputDto.getAnswerId())) {
            throw new UserException(ErrorCodeAndMessage.ANSWER_NOT_FOUND_CODE, ErrorCodeAndMessage.ANSWER_IMAGE_NOT_FOUND_MESSAGE);
        }

        int currentUserId = generalService.getCurrentUserId();

        UserSingleQuestionAnswerEntity userSingleQuestionAnswerEntity = new UserSingleQuestionAnswerEntity();
        userSingleQuestionAnswerEntity.setUserId(currentUserId);
        userSingleQuestionAnswerEntity.setMultipleChoiceQuestionAnswerId(inputDto.getAnswerId());
        userSingleQuestionAnswerRepository.save(userSingleQuestionAnswerEntity);
    }

    @Transactional
    public void fillRangeAnswer(RangeAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.Range);

        int currentUserId = generalService.getCurrentUserId();
        if (userRangeQuestionAnswerRepository.existsByUserIdAndEssentialQuestionId(currentUserId, inputDto.getQuestionId())) {
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
    public void fillOpenNumberAnswer(OpenNumberAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.OpenNumber);

        int currentUserId = generalService.getCurrentUserId();
        if (userOpenNumberQuestionAnswerRepository.existsByUserIdAndEssentialQuestionId(currentUserId, inputDto.getQuestionId())) {
            throw new UserException(ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE,
                    ErrorCodeAndMessage.USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE);
        }

        UserOpenNumberQuestionAnswerEntity userOpenNumberQuestionAnswerEntity = new UserOpenNumberQuestionAnswerEntity();
        userOpenNumberQuestionAnswerEntity.setUserId(currentUserId);
        userOpenNumberQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
        userOpenNumberQuestionAnswerEntity.setValue(inputDto.getValue());
        userOpenNumberQuestionAnswerRepository.save(userOpenNumberQuestionAnswerEntity);
    }

    public void fillImageAnswer(ImageAnswerInputDto inputDto) {
        validateQuestionExistence(inputDto.getQuestionId(), QuestionType.Image);

        int currentUserId = generalService.getCurrentUserId();

        List<UserImageQuestionAnswerEntity> entityList = new ArrayList<>();
        inputDto.getImageList().forEach(image -> {
            UserImageQuestionAnswerEntity userImageQuestionAnswerEntity = new UserImageQuestionAnswerEntity();
            userImageQuestionAnswerEntity.setUserId(currentUserId);
            userImageQuestionAnswerEntity.setEssentialQuestionId(inputDto.getQuestionId());
            userImageQuestionAnswerEntity.setImage(image);
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
            if (!questionEntityOptional.get().getQuestionOwnerType().equals(QuestionOwnerType.Public) &&
                    !ownerType.get().equals(questionEntityOptional.get().getQuestionOwnerType())) {
                throw new UserException(ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_CODE, ErrorCodeAndMessage.QUESTION_TYPE_DOESNT_MATCH_MESSAGE);
            }
        }
        return questionEntityOptional.get();
    }

    public List<UserQuestionAnswerOutputDto> getUserAnswersByUser() {
        return operatorService.getUserAnswers(generalService.getCurrentUserId());
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

        userOpenNumberQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        allAnswerOpenDto.getOpenNumberAnswerInputDtoList().forEach(this::fillOpenNumberAnswer);

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

    public List<ImageAnswerOutputDto> getUserImageAnswerByUser(int questionId) {
        return getUserImageAnswerByQuestionId(generalService.getCurrentUserId(), questionId);
    }

    public List<ImageAnswerOutputDto> getUserImageAnswerByOperator(int userId, int questionId) {
        return getUserImageAnswerByQuestionId(userId, questionId);
    }

    private List<ImageAnswerOutputDto> getUserImageAnswerByQuestionId(int userId, int questionId) {
        List<UserImageQuestionAnswerEntity> imageList = userImageQuestionAnswerRepository.findAllByUserIdAndEssentialQuestionId(userId, questionId);
        List<ImageAnswerOutputDto> outputDtoList = new ArrayList<>();
        imageList.forEach(image -> {
            ImageAnswerOutputDto outputDto = new ImageAnswerOutputDto();
            outputDto.setImage(image.getImage());
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
        outputDto.setImage(entity.get().getImage());
        return outputDto;
    }

    @Transactional
    public void deleteAllAnswers() {
        int currentUserId = generalService.getCurrentUserId();
        userImageQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userMultipleQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userRangeQuestionAnswerRepository.deleteAllByUserId(currentUserId);
        userOpenNumberQuestionAnswerRepository.deleteAllByUserId(currentUserId);
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

    @Transactional
    public void deleteAllImageAnswers() {
        userImageQuestionAnswerRepository.deleteAllByUserId(generalService.getCurrentUserId());
    }
}
