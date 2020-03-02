package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.operator.DoctorOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorStateInputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperatorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final GeneralService generalService;

    public OperatorService(DoctorRepository doctorRepository, PatientRepository patientRepository, GeneralService generalService) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.generalService = generalService;
    }

    public void fillPatientState(PatientStateInputDto patientStateInputDto) {
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(patientStateInputDto.getPatientId());
        if (patientUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        patientUserEntityOptional.get().setPatientStateType(patientStateInputDto.getPatientStateType());
        patientRepository.save(patientUserEntityOptional.get());
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
                .map(PatientOutputDto::fromEntity)
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
                .map(DoctorOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void fillDoctorState(DoctorStateInputDto doctorStateInputDto) {
        Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(doctorStateInputDto.getDoctorId());
        if (doctorUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        doctorUserEntityOptional.get().setDoctorStateType(doctorStateInputDto.getDoctorStateType());
        doctorRepository.save(doctorUserEntityOptional.get());
    }
}
