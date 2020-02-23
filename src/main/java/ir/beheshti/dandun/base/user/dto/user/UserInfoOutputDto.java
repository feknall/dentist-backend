package ir.beheshti.dandun.base.user.dto.user;

import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.util.SexType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

@Data
public class UserInfoOutputDto {
    private int id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
    private Timestamp registerTime;
    private String birthPlace;
    private String job;
    private String address;
    private String telephone;
    private String fatherName;
    private SexType sex;
    private String educationLevel;
    private String marriageStatus;
    private Timestamp birthDate;

    public static UserInfoOutputDto fromEntity(UserEntity entity) {
        UserInfoOutputDto outputDto = new UserInfoOutputDto();
        BeanUtils.copyProperties(entity, outputDto);
        return outputDto;
    }
}
