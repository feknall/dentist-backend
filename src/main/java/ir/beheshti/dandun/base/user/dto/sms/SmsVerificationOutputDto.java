package ir.beheshti.dandun.base.user.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsVerificationOutputDto {
    private Boolean isNewUser;
    private Boolean isValid;
}
