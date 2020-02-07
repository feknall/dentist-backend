package ir.beheshti.dandun.base.user.dto.login;

import lombok.Data;

@Data
public class LoginOutputDto {
    private String accessToken;
    private String refreshToken;
}
