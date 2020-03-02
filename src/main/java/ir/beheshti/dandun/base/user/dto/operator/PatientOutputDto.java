package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

@Data
public class PatientOutputDto {
    private int patientId;
    private String firstName;
    private String lastName;
    private PatientStateType patientStateType;

    public static PatientOutputDto fromEntity(PatientUserEntity entity) {
        PatientOutputDto dto = new PatientOutputDto();
        dto.setPatientId(entity.getPatientId());
        dto.setFirstName(entity.getUserEntity().getFirstName());
        dto.setLastName(entity.getUserEntity().getLastName());
        dto.setPatientStateType(entity.getPatientStateType());
        return dto;
    }
}
