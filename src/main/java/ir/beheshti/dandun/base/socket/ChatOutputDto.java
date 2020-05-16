package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.entity.ChatEntity;
import lombok.Data;

@Data
public class ChatOutputDto {
    private Integer chatId;
    private Integer patientId;
    private Integer doctorId;
    private String lastMessage;
    private ChatStateType chatStateType;

    public static ChatOutputDto fromEntity(ChatEntity entity) {
        ChatOutputDto dto = new ChatOutputDto();
        dto.setChatId(entity.getChatId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setPatientId(entity.getPatientId());
        dto.setLastMessage(entity.getMessageEntityList().get(entity.getMessageEntityList().size() - 1).getMessage());
        dto.setChatStateType(entity.getChatStateType());
        return dto;
    }
}
