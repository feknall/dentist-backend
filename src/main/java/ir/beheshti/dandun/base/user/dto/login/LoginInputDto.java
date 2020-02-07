package ir.beheshti.dandun.base.user.dto.login;

import ir.beheshti.dandun.base.user.common.EnumNamePattern;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginInputDto {
    @NotNull
    @Size(min = 12, max = 12)
    private String phoneNumber;
    @NotNull
    @Size(min = 4, max = 4)
    private String verificationCode;
}
