package ir.beheshti.dandun.base.user.dto.signup;

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
    @Size(min = 1, max = 3)
    private Integer userType;
}
