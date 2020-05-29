package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.user.entity.ChatEntity;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.Collections;

@Log4j2
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
    private boolean lastIsBinary = false;
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
        MessageEntity lastMessage = entity.getMessageEntityList().get(entity.getMessageEntityList().size() - 1);
        if (lastMessage.getMessage() != null) {
            dto.setLastMessage(lastMessage.getMessage());
        } else {
            dto.setLastIsBinary(true);
        }
        dto.setChatStateType(entity.getChatStateType());
        return dto;
    }

    public static ChatOutputDto fromEntity(ChatEntity entity, MessageEntity lastMessage) {
        entity.setMessageEntityList(Collections.singletonList(lastMessage));
        return fromEntity(entity);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e);
            return null;
        }
    }
}
