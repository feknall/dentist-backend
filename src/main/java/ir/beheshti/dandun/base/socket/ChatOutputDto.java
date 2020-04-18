package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.entity.ChatEntity;
import lombok.Data;

@Data
public class ChatOutputDto {
    private Integer chatId;
    private Integer patientId;
    private Integer doctorId;
    private Integer messageId;

    public static ChatOutputDto fromEntity(ChatEntity entity) {
        ChatOutputDto dto = new ChatOutputDto();
        dto.setChatId(entity.getChatId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setPatientId(entity.getPatientId());
        dto.setMessageId(entity.getMessageId());
        return dto;
    }
}
