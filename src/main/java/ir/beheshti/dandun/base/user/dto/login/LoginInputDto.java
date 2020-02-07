package ir.beheshti.dandun.base.user.dto.login;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginInputDto {
    @Size(min = 12, max = 12)
    private String phoneNumber;
    @Size(min = 4, max = 4)
    private String verificationCode;
}
