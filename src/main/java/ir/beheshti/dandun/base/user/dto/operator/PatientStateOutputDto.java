package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

@Data
public class PatientStateOutputDto {
    private Integer patientId;
    private PatientStateType patientStateType;

    public static PatientStateOutputDto fromEntity(PatientUserEntity patientUserEntity) {
        PatientStateOutputDto dto = new PatientStateOutputDto();
        dto.patientId = patientUserEntity.getPatientId();
        dto.setPatientStateType(patientUserEntity.getPatientStateType());
        return dto;
    }
}
