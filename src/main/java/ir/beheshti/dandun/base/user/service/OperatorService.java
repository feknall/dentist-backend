package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.operator.DoctorOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorStateInputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.dto.question.UserQuestionAnswerOutputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.entity.UserMultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OperatorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PushNotificationService pushNotificationService;

    public void fillPatientState(PatientStateInputDto patientStateInputDto) {
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(patientStateInputDto.getPatientId());
        if (patientUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        patientUserEntityOptional.get().setPatientStateType(patientStateInputDto.getPatientStateType());
        patientRepository.save(patientUserEntityOptional.get());
        pushNotificationService.sendChangePatientStateNotification(patientUserEntityOptional.get().getPatientId());
    }

    public PatientOutputDto getPatientStateByUser() {
        return getPatientState(generalService.getCurrentUserId());
    }

    public PatientOutputDto getPatientStateByOperator(int patientId) {
        return getPatientState(patientId);
    }

    public DoctorOutputDto getDoctorStateByUser() {
        return getDoctorState(generalService.getCurrentUserId());
    }

    public DoctorOutputDto getDoctorStateByOperator(int doctorId) {
        return getDoctorState(doctorId);
    }

    private DoctorOutputDto getDoctorState(int doctorId) {
        Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(doctorId);
        if (doctorUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return DoctorOutputDto.fromEntity(doctorUserEntityOptional.get());
    }

    private PatientOutputDto getPatientState(int patientId) {
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(patientId);
        if (patientUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return PatientOutputDto.fromEntity(patientUserEntityOptional.get());
    }

    public List<PatientOutputDto> getPatientsList(PatientStateType patientStateType) {
        List<PatientUserEntity> patientUserEntityList;
        if (patientStateType == null) {
            patientUserEntityList = patientRepository.findAll();
        } else {
            patientUserEntityList = patientRepository.findAllByPatientStateType(patientStateType);
        }

        return patientUserEntityList
                .stream()
                .map(PatientOutputDto::fromEntityMinimum)
                .collect(Collectors.toList());
    }

    public List<DoctorOutputDto> getDoctorList(DoctorStateType doctorStateType) {
        List<DoctorUserEntity> doctorUserEntityList;
        if (doctorStateType == null) {
            doctorUserEntityList = doctorRepository.findAll();
        } else {
            doctorUserEntityList = doctorRepository.findAllByDoctorStateType(doctorStateType);
        }

        return doctorUserEntityList
                .stream()
                .map(DoctorOutputDto::fromEntityMinimum)
                .collect(Collectors.toList());
    }

    public void fillDoctorState(DoctorStateInputDto doctorStateInputDto) {
        Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(doctorStateInputDto.getDoctorId());
        if (doctorUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        doctorUserEntityOptional.get().setDoctorStateType(doctorStateInputDto.getDoctorStateType());
        doctorRepository.save(doctorUserEntityOptional.get());
        pushNotificationService.sendChangeDoctorStateNotification(doctorUserEntityOptional.get().getDoctorId());
    }

    public List<UserQuestionAnswerOutputDto> getUserAnswers(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }

        List<UserQuestionAnswerOutputDto> outputDtoList = new ArrayList<>();

        // Open Answers

        outputDtoList.addAll(userEntity
                .get()
                .getUserOpenQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofOpenAnswer)
                .collect(Collectors.toList()));

        // Single-Choice Answers
        outputDtoList.addAll(userEntity
                .get()
                .getUserSingleQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofSingle)
                .collect(Collectors.toList()));

        // Range Answers
        outputDtoList.addAll(userEntity
                .get()
                .getUserRangeQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofRange)
                .collect(Collectors.toList()));

        outputDtoList.addAll(userEntity.get()
                .getUserOpenNumberQuestionAnswerEntityList()
                .stream()
                .map(UserQuestionAnswerOutputDto::ofOpenNumber)
                .collect(Collectors.toList()));

        // Multiple-Choice Answers
        List<UserMultipleChoiceQuestionAnswerEntity> answersForSameQuestion;
        int size = userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().size();
        List<Integer> visitedQuestions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            answersForSameQuestion = new ArrayList<>();
            int answer1 = userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().get(i).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId();
            if (visitedQuestions.contains(answer1))
                continue;
            for (int j = i; j < size; j++) {
                int answer2 = userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().get(j).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId();
                if (answer1 == answer2) {
                    answersForSameQuestion.add(userEntity.get().getUserMultipleChoiceQuestionAnswerEntityList().get(j));
                    visitedQuestions.add(answer1);
                }
            }
            outputDtoList
                    .add(UserQuestionAnswerOutputDto.ofMultipleChoice(answersForSameQuestion));
        }

        return outputDtoList;
    }

}
