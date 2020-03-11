package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DoctorOutputDto {
    private int doctorId;
    private String firstName;
    private String lastName;
    private DoctorStateType doctorStateType;
    private Timestamp registrationTime;
    private String phoneNumber;
    private String image;

    private static DoctorOutputDto fromEntity(DoctorUserEntity entity, boolean fillImage) {
        DoctorOutputDto dto = new DoctorOutputDto();
        dto.setDoctorId(entity.getDoctorId());
        dto.setFirstName(entity.getUserEntity().getFirstName());
        dto.setLastName(entity.getUserEntity().getLastName());
        dto.setDoctorStateType(entity.getDoctorStateType() != null ? entity.getDoctorStateType() : DoctorStateType.PENDING);
        dto.setRegistrationTime(entity.getUserEntity().getRegistrationTime());
        dto.setPhoneNumber(entity.getUserEntity().getPhoneNumber());
        if (fillImage) {
            dto.setImage(entity.getUserEntity().getProfilePhoto());
        }
        return dto;
    }

    public static DoctorOutputDto fromEntityMinimum(DoctorUserEntity entity) {
        return DoctorOutputDto.fromEntity(entity, false);
    }

    public static DoctorOutputDto fromEntity(DoctorUserEntity entity) {
        return DoctorOutputDto.fromEntity(entity, true);
    }

}
