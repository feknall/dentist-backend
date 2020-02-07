package ir.beheshti.dandun.base.user.dto.signup;

import ir.beheshti.dandun.base.user.common.EnumNamePattern;
import ir.beheshti.dandun.base.user.util.UserType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpInputDto {
    @NotNull
    @Size(min = 12, max = 12)
    private String phoneNumber;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @EnumNamePattern(regexp = "Patient|Doctor|Operator")
    private UserType userType;
}
