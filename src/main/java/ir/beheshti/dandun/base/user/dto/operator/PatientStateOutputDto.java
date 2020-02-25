package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PatientStateOutputDto {
    private Integer patientId;
    private PatientStateType patientStateType;

    public static PatientStateOutputDto fromEntity(PatientUserEntity patientUserEntity) {
        PatientStateOutputDto dto = new PatientStateOutputDto();
        dto.patientId = patientUserEntity.getPatientId();
        dto.setPatientStateType(patientUserEntity.getPatientStateType() != null ? patientUserEntity.getPatientStateType() : PatientStateType.UNCATEGORIZED);
        return dto;
    }
}
