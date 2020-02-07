package ir.beheshti.dandun.base.user.dto.sms;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SmsInputDto {
    @Size(min = 12, max = 12)
    private String phoneNumber;
}
