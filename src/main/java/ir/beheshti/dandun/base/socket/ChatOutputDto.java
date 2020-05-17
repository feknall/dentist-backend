package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.entity.ChatEntity;
import lombok.Data;

@Data
public class ChatOutputDto {
    private Integer chatId;

    private Integer patientId;
    private String patientFirstName;
    private String patientLastName;
    private String patientPicture;

    private Integer doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorPicture;

    private String lastMessage;
    private ChatStateType chatStateType;

    public static ChatOutputDto fromEntity(ChatEntity entity) {
        ChatOutputDto dto = new ChatOutputDto();
        dto.setChatId(entity.getChatId());
        dto.setDoctorId(entity.getDoctorId());
        if (entity.getDoctorEntity() != null) {
            dto.setDoctorFirstName(entity.getDoctorEntity().getFirstName());
            dto.setDoctorLastName(entity.getDoctorEntity().getLastName());
            dto.setDoctorPicture(entity.getDoctorEntity().getProfilePhoto());
        }
        if (entity.getPatientEntity() != null) {
            dto.setPatientFirstName(entity.getPatientEntity().getFirstName());
            dto.setPatientLastName(entity.getPatientEntity().getLastName());
            dto.setPatientPicture(entity.getPatientEntity().getProfilePhoto());
        }
        dto.setPatientId(entity.getPatientId());
        dto.setLastMessage(entity.getMessageEntityList().get(entity.getMessageEntityList().size() - 1).getMessage());
        dto.setChatStateType(entity.getChatStateType());
        return dto;
    }
}
