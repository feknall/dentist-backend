package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import lombok.Data;

@Data
public class DoctorOutputDto {
    private int doctorId;
    private String firstName;
    private String lastName;
    private DoctorStateType doctorStateType;

    public static DoctorOutputDto fromEntity(DoctorUserEntity entity) {
        DoctorOutputDto dto = new DoctorOutputDto();
        dto.setDoctorId(entity.getDoctorId());
        dto.setFirstName(entity.getUserEntity().getFirstName());
        dto.setLastName(entity.getUserEntity().getLastName());
        dto.setDoctorStateType(entity.getDoctorStateType() != null ? entity.getDoctorStateType() : DoctorStateType.PENDING);
        return dto;
    }

}
