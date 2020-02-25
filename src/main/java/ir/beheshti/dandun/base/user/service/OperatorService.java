package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateOutputDto;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperatorService {

    @Autowired
    private PatientRepository patientRepository;

    public void fillPatientState(PatientStateInputDto patientStateInputDto) {
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(patientStateInputDto.getPatientId());
        if (patientUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        patientUserEntityOptional.get().setPatientStateType(patientStateInputDto.getPatientStateType());
        patientRepository.save(patientUserEntityOptional.get());
    }

    public PatientOutputDto getPatientState(int patientId) {
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
}
