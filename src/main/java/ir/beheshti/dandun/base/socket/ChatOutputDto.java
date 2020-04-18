package ir.beheshti.dandun.base.socket;

import lombok.Data;

@Data
public class ChatOutputDto {
    private Integer patientId;
    private Integer doctorId;
    private Integer messageId;
}
