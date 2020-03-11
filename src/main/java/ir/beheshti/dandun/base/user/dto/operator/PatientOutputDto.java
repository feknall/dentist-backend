package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientOutputDto {
    private int patientId;
    private String firstName;
    private String lastName;
    private PatientStateType patientStateType;
    private String phoneNumber;
    private Timestamp registrationTime;
    private String image;

    private static PatientOutputDto fromEntity(PatientUserEntity entity, boolean fillImage) {
        PatientOutputDto dto = new PatientOutputDto();
        dto.setPatientId(entity.getPatientId());
        dto.setFirstName(entity.getUserEntity().getFirstName());
        dto.setLastName(entity.getUserEntity().getLastName());
        dto.setPatientStateType(entity.getPatientStateType());
        dto.setPhoneNumber(entity.getUserEntity().getPhoneNumber());
        dto.setRegistrationTime(entity.getUserEntity().getRegistrationTime());
        if (fillImage) {
            dto.setImage(entity.getUserEntity().getProfilePhoto());
        }
        return dto;
    }

    public static PatientOutputDto fromEntityMinimum(PatientUserEntity entity) {
        return PatientOutputDto.fromEntity(entity, false);
    }

    public static PatientOutputDto fromEntity(PatientUserEntity entity) {
        return PatientOutputDto.fromEntity(entity, true);
    }
}
