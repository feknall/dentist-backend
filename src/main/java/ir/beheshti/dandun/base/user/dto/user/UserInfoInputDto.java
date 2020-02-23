package ir.beheshti.dandun.base.user.dto.user;

import ir.beheshti.dandun.base.user.util.SexType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfoInputDto {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthPlace;
    private String job;
    private String address;
    private String telephone;
    private String fatherName;
    private SexType sex;
    private String educationLevel;
    private String marriageStatus;
    private Timestamp birthDate;
}
